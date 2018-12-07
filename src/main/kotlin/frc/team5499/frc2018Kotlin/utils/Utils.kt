package frc.team5499.frc2018Kotlin.utils

import frc.team5499.frc2018Kotlin.Constants

@SuppressWarnings("MagicNumber", "TooManyFunctions")
object Utils {

    public fun epsilonEquals(a: Double, b: Double, epsilon: Double): Boolean {
        return (a - epsilon <= b) && (a + epsilon >= b)
    }

    public fun epsilonEquals(a: Double, b: Double): Boolean {
        return epsilonEquals(a, b, Constants.EPSILON)
    }

    public fun epsilonEquals(a: Int, b: Int, epsilon: Int): Boolean {
        return (a - epsilon <= b) && (a + epsilon >= b)
    }

    fun limit(value: Double, limit: Double): Double {
        return limit(value, -limit, limit)
    }

    fun limit(value: Double, min: Double, max: Double): Double {
        return Math.min(max, Math.max(min, value))
    }

    fun interpolate(a: Double, b: Double, x: Double): Double {
        val newX = limit(x, 0.0, 1.0)
        return a + (b - a) * newX
    }

    fun inchesPerSecondToEncoderTicksPer100Ms(ips: Double): Double {
        return inchesToEncoderTicks(ips) / 10.0
    }

    fun encoderTicksPer100MsToInchesPerSecond(eps: Int): Double {
        return encoderTicksToInches(eps) * 10.0
    }

    fun inchesToEncoderTicks(inches: Double): Int {
        // val temp = (Constants.ENCODERS_TICKS_PER_ROTATION / Constants.WHEEL_CIR ) * inches
        return ((Constants.ENCODER_TICKS_PER_ROTATION / Constants.WHEEL_CIR) * inches).toInt()
    }

    fun encoderTicksToInches(ticks: Int): Double {
        return (Constants.WHEEL_CIR / Constants.ENCODER_TICKS_PER_ROTATION) * ticks
    }

    fun talonAngleToDegrees(ticks: Int): Double {
        return (360.0 / Constants.Gyro.TURN_UNITS_PER_ROTATION) * ticks
    }

    fun degreesToTalonAngle(degrees: Double): Int {
        return ((Constants.Gyro.TURN_UNITS_PER_ROTATION / 360.0) * degrees).toInt()
    }
}
