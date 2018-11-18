package frc.team5499.frc2018Kotlin.utils

import frc.team5499.frc2018Kotlin.Constants

class DriveHelper {

    companion object {
        // cheesy
        private var kThrottleDeadband = 0.02
        private var kWheelDeadband = 0.02

        private var kHighWheelNonLinearity = 0.65
        private var kLowWheelNonLinearity = 0.65

        private var kHighNegInertiaScalar = 4.0
        private var kLowNegInertiaThreshold = 0.65
        private var kLowNegInertiaTurnScalar = 3.5
        private var kLowNegInertiaCloseScalar = 4.0
        private var kLowNegInertiaFarScalar = 5.0

        private var kHighSensitivity = 0.65
        private var kLowSensitiity = 0.65

        private var kQuickStopDeadband = 0.5
        private var kQuickStopWeight = 0.1
        private var kQuickStopScalar = 5.0

        // space
        private var kTurnMult = 0.6

        // tank
        private var kSlowMult = 0.4
    }

    private var mOldWheel = 0.0
    private var mQuickStopAccumlator = 0.0
    private var mNegInertiaAccumlator = 0.0
    private var mDebugCounter = 0

    fun spaceDrive(throttle: Double, wheel: Double, isQuickTurn: Boolean): DriveSignal {
        val newThottle = handleDeadband(throttle, kThrottleDeadband)
        var newWheel = handleDeadband(wheel, kWheelDeadband)
        val mult = if (!isQuickTurn) kTurnMult else 1.0
        newWheel *= mult
        return DriveSignal(newThottle + newWheel, newThottle - newWheel)
    }

    fun tankDrive(left: Double, right: Double, isSlow: Boolean): DriveSignal {
        val newLeft = handleDeadband(left, kThrottleDeadband)
        val newRight = handleDeadband(right, kThrottleDeadband)
        val mult = if (isSlow) kSlowMult else 1.0
        return DriveSignal(newLeft * mult, newRight * mult)
    }

    @Suppress("LongMethod", "ComplexMethod")
    fun cheesyDrive(throttle: Double, wheel: Double, isQuickTurn: Boolean, isHighGear: Boolean): DriveSignal {
        var newWheel = handleDeadband(wheel, Constants.Input.DEADBAND)
        var newThrottle = handleDeadband(throttle, Constants.Input.DEADBAND)

        val negInertia = newWheel - mOldWheel
        mOldWheel = newWheel

        val wheelNonLinearity: Double
        if (isHighGear) {
            wheelNonLinearity = kHighWheelNonLinearity
            val denominator = Math.sin(Math.PI / 2.0 * wheelNonLinearity)
            newWheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * newWheel) / denominator
            newWheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * newWheel) / denominator
        } else {
            wheelNonLinearity = kLowWheelNonLinearity
            val denominator = Math.sin(Math.PI / 2.0 * wheelNonLinearity)
            newWheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * newWheel) / denominator
            newWheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * newWheel) / denominator
            newWheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * newWheel) / denominator
        }

        var leftPwm: Double
        var rightPwm: Double
        var overPower: Double
        var sensitivity: Double
        var angularPower: Double
        var linearPower: Double

        var negInertiaScalar: Double

        if (isHighGear) {
            negInertiaScalar = kHighNegInertiaScalar
            sensitivity = kHighSensitivity
        } else {
            if (wheel * negInertia > 0) {
                // If we are moving away from 0.0, aka, trying to get more wheel.
                negInertiaScalar = kLowNegInertiaTurnScalar
            } else {
                // Otherwise, we are attempting to go back to 0.0.
                if (Math.abs(wheel) > kLowNegInertiaThreshold) {
                    negInertiaScalar = kLowNegInertiaFarScalar
                } else {
                    negInertiaScalar = kLowNegInertiaCloseScalar
                }
            }
            sensitivity = kLowSensitiity
        }
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
        linearPower = throttle

        if (isQuickTurn) {
            if (Math.abs(linearPower) < kQuickStopDeadband) {
                val alpha = kQuickStopWeight
                mQuickStopAccumlator = (1 - alpha) * mQuickStopAccumlator
                        + alpha * Utils.limit(wheel, 1.0) * kQuickStopScalar
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

    private fun handleDeadband(value: Double, deadband: Double): Double {
        return if (Math.abs(value) > Math.abs(deadband)) value else 0.0
    }
}
