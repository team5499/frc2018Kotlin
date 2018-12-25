package frc.team5499.frc2018Kotlin.utils

import frc.team5499.frc2018Kotlin.Constants

object DriveHelper {

    private var mOldWheel = 0.0
    private var mQuickStopAccumlator = 0.0
    private var mNegInertiaAccumlator = 0.0
    private var mDebugCounter = 0

    fun spaceDrive(throttle: Double, wheel: Double, isQuickTurn: Boolean): DriveSignal {
        val newThottle = handleDeadband(throttle, Constants.Input.THROTTLE_DEADBAND)
        var newWheel = handleDeadband(wheel, Constants.Input.WHEEL_DEADBAND)
        val mult = if (!isQuickTurn) Constants.Input.TURN_MULT else 1.0
        newWheel *= mult
        return DriveSignal(newThottle + newWheel, newThottle - newWheel)
    }

    fun tankDrive(left: Double, right: Double, isSlow: Boolean): DriveSignal {
        val newLeft = handleDeadband(left, Constants.Input.THROTTLE_DEADBAND)
        val newRight = handleDeadband(right, Constants.Input.THROTTLE_DEADBAND)
        val mult = if (isSlow) Constants.Input.SLOW_MULT else 1.0
        return DriveSignal(newLeft * mult, newRight * mult)
    }

    @Suppress("LongMethod", "ComplexMethod")
    fun cheesyDrive(throttle: Double, wheel: Double, isQuickTurn: Boolean, isHighGear: Boolean): DriveSignal {
        var newWheel = handleDeadband(wheel, Constants.Input.WHEEL_DEADBAND)
        var newThrottle = handleDeadband(throttle, Constants.Input.THROTTLE_DEADBAND)

        val negInertia = newWheel - mOldWheel
        mOldWheel = newWheel

        newWheel = calculateCheesyNonlinearity(newWheel, isHighGear)

        var leftPwm: Double
        var rightPwm: Double
        var overPower: Double
        var angularPower: Double
        var linearPower: Double

        val pair = calculateCheesyNegIntertialScalar(newWheel, negInertia, isHighGear)
        val negInertiaScalar = pair.first
        val sensitivity = pair.second

        val negInertiaPower = negInertia * negInertiaScalar
        mNegInertiaAccumlator += negInertiaPower

        newWheel = newWheel + mNegInertiaAccumlator
        if (mNegInertiaAccumlator > 1) {
            mNegInertiaAccumlator -= 1
        } else if (mNegInertiaAccumlator < -1) {
            mNegInertiaAccumlator += 1
        } else {
            mNegInertiaAccumlator = 0.0
        }
        linearPower = newThrottle

        if (isQuickTurn) {
            if (Math.abs(linearPower) < Constants.Input.QUICKSTOP_DEADBAND) {
                val alpha = Constants.Input.QUICKSTOP_WEIGHT
                mQuickStopAccumlator = (1 - alpha) * mQuickStopAccumlator
                        + alpha * Utils.limit(wheel, 1.0) * Constants.Input.QUICKSTOP_SCALAR
            }
            overPower = 1.0
            angularPower = newWheel
        } else {
            overPower = 0.0
            angularPower = Math.abs(throttle) * newWheel * sensitivity - mQuickStopAccumlator
            if (mQuickStopAccumlator > 1) {
                mQuickStopAccumlator -= 1
            } else if (mQuickStopAccumlator < -1) {
                mQuickStopAccumlator += 1
            } else {
                mQuickStopAccumlator = 0.0
            }
        }
        leftPwm = linearPower
        rightPwm = linearPower
        leftPwm += angularPower
        rightPwm -= angularPower

        if (leftPwm > 1.0) {
            rightPwm -= overPower * (leftPwm - 1.0)
            leftPwm = 1.0
        } else if (rightPwm > 1.0) {
            leftPwm -= overPower * (rightPwm - 1.0)
            rightPwm = 1.0
        } else if (leftPwm < -1.0) {
            rightPwm += overPower * (-1.0 - leftPwm)
            leftPwm = -1.0
        } else if (rightPwm < -1.0) {
            leftPwm += overPower * (-1.0 - rightPwm)
            rightPwm = -1.0
        }

        return DriveSignal(leftPwm, rightPwm)
    }

    private fun calculateCheesyNonlinearity(wheel: Double, isHighGear: Boolean): Double {
        var newWheel = wheel
        if (isHighGear) {
            val wheelNonLinearity = Constants.Input.HIGH_WHEEL_NONLINEARITY
            val denominator = Math.sin(Math.PI / 2.0 * wheelNonLinearity)
            newWheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * newWheel) / denominator
            newWheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * newWheel) / denominator
        } else {
            val wheelNonLinearity = Constants.Input.LOW_WHEEL_NONLINEARITY
            val denominator = Math.sin(Math.PI / 2.0 * wheelNonLinearity)
            newWheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * newWheel) / denominator
            newWheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * newWheel) / denominator
            newWheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * newWheel) / denominator
        }
        return newWheel
    }

    private fun calculateCheesyNegIntertialScalar(
        wheel: Double,
        negInertia: Double,
        isHighGear: Boolean
    ): Pair<Double, Double> {
        val sensitivity: Double
        val negInertiaScalar: Double
        if (isHighGear) {
            negInertiaScalar = Constants.Input.HIGH_NEGINERTIA_SCALAR
            sensitivity = Constants.Input.HIGH_SENSITIVITY
        } else {
            if (wheel * negInertia > 0) {
                // If we are moving away from 0.0, aka, trying to get more wheel.
                negInertiaScalar = Constants.Input.LOW_NEGINERTIA_TURN_SCALAR
            } else {
                // Otherwise, we are attempting to go back to 0.0.
                if (Math.abs(wheel) > Constants.Input.LOW_NEGINERTIA_THRESHOLD) {
                    negInertiaScalar = Constants.Input.LOW_NEGINERTIA_FAR_SCALAR
                } else {
                    negInertiaScalar = Constants.Input.LOW_NEGINERTIA_CLOSE_SCALAR
                }
            }
            sensitivity = Constants.Input.LOW_SENSITIVITY
        }
        return Pair<Double, Double>(negInertiaScalar, sensitivity)
    }

    private fun handleDeadband(value: Double, deadband: Double): Double {
        return if (Math.abs(value) > Math.abs(deadband)) value else 0.0
    }
}
