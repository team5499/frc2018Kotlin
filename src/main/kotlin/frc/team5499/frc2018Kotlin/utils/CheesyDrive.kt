package frc.team5499.frc2018Kotlin.utils

import frc.team5499.frc2018Kotlin.Constants

class CheesyDrive {

    private var mLastTurn = 0.0
    private var mQuickStopAccumlator = 0.0
    private var mNegInertiaAccumlator = 0.0
    private var mDebugCounter = 0

    @Suppress("LongMethod", "ComplexMethod")
    fun cheesyDrive(throttle: Double, turn: Double, isQuickTurn: Boolean): DriveSignal {
        var newTurn = handleDeadband(turn, Constants.Input.DEADBAND)
        var newThrottle = handleDeadband(throttle, Constants.Input.DEADBAND)

        val negInertia = newTurn - mLastTurn
        mLastTurn = newTurn

        val turnNonLinearity: Double = Constants.Input.WHEEL_NON_LINEARITY
        val denominator = Math.sin(Math.PI / 2.0 * turnNonLinearity)
        newTurn = Math.sin(Math.PI / 2.0 * turnNonLinearity * newTurn) / denominator
        newTurn = Math.sin(Math.PI / 2.0 * turnNonLinearity * newTurn) / denominator

        val negInertiaScalar = Constants.Input.INERTIA_SCALAR
        val sensitivity = Constants.Input.SENSITIVITY
        val negInertiaPower = negInertia * negInertiaScalar

        mNegInertiaAccumlator += negInertiaPower

        newTurn = newTurn + mNegInertiaAccumlator
        if (mNegInertiaAccumlator > 1) {
            mNegInertiaAccumlator -= 1
        } else if (mNegInertiaAccumlator < -1) {
            mNegInertiaAccumlator += 1
        } else {
            mNegInertiaAccumlator = 0.0
        }
        val linearPower = newThrottle

        val overpower: Double
        val angularPower: Double

        if (isQuickTurn) {
            if (Math.abs(linearPower) < Constants.Input.QUICK_STOP_DEADBAND) {
                val alpha = Constants.Input.QUICK_STOP_WEIGHT
                mQuickStopAccumlator = (1 - alpha) * mQuickStopAccumlator
                    + alpha * Utils.limit(newTurn, 1.0) * Constants.Input.QUICK_STOP_SCALAR
            }
            overpower = 1.0
            angularPower = newTurn
        } else {
            overpower = 0.0
            angularPower = Math.abs(throttle) * newTurn * sensitivity - mQuickStopAccumlator
            if (mQuickStopAccumlator > 1.0) {
                mQuickStopAccumlator -= 1.0
            } else if (mQuickStopAccumlator < -1.0) {
                mQuickStopAccumlator += 1.0
            } else {
                mQuickStopAccumlator = 0.0
            }
        }

        var rightPWM = linearPower
        var leftPWM = linearPower
        leftPWM += angularPower
        rightPWM -= angularPower

        if (leftPWM > 1.0) {
            rightPWM -= overpower * (leftPWM - 1.0)
            leftPWM = 1.0
        } else if (rightPWM > 1.0) {
            leftPWM -= overpower * (rightPWM - 1.0)
            rightPWM = 1.0
        } else if (leftPWM < -1.0) {
            rightPWM += overpower * (-1 - leftPWM)
            leftPWM = -1.0
        } else if (rightPWM < -1.0) {
            leftPWM += overpower * (-1.0 - rightPWM)
            rightPWM = -1.0
        }
        @Suppress("MagicNumber")
        if (mDebugCounter == 50) {
            @Suppress("MaxLineLength")
            // println("Quick Turn: $isQuickTurn, Angular Power: $angularPower, Quick Stop Accumulator: $mQuickStopAccumlator")
            println("Left PWM: $leftPWM, Right PWM: $rightPWM")
            mDebugCounter = 0
        } else {
            mDebugCounter++
        }

        return DriveSignal(leftPWM, rightPWM, false)
    }

    private fun handleDeadband(value: Double, deadband: Double): Double {

        return if (Math.abs(value) > Math.abs(deadband)) value else 0.0
    }
}
