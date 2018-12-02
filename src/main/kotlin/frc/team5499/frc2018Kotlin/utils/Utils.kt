package frc.team5499.frc2018Kotlin.utils

import frc.team5499.frc2018Kotlin.Constants

object Utils {

    const val INCHES_PER_SECOND_PER_ENCODER_TICKS_PER_100MS = 10.0
    const val DEGREES_PER_ROTATION = 360.0

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
        return inchesToEncoderTicks(ips) / INCHES_PER_SECOND_PER_ENCODER_TICKS_PER_100MS
    }

    fun encoderTicksPer100MsToInchesPerSecond(eps: Int): Double {
        return eps * INCHES_PER_SECOND_PER_ENCODER_TICKS_PER_100MS
    }

    fun inchesToEncoderTicks(inches: Double): Int {
        // val temp = (Constants.ENCODERS_TICKS_PER_ROTATION / Constants.WHEEL_CIR ) * inches
        return ((Constants.ENCODER_TICKS_PER_ROTATION / Constants.WHEEL_CIR) * inches).toInt()
    }

    fun encoderTicksToInches(ticks: Int): Double {
        return (Constants.WHEEL_CIR / Constants.ENCODER_TICKS_PER_ROTATION) * ticks
    }

    fun talonAngleToDegrees(ticks: Int): Double {
        return (DEGREES_PER_ROTATION / Constants.Gyro.TURN_UNITS_PER_ROTATION) * ticks
    }

    fun degreesToTalonAngle(degrees: Double): Int {
        return ((Constants.Gyro.TURN_UNITS_PER_ROTATION / DEGREES_PER_ROTATION) * degrees).toInt()
    }
}
