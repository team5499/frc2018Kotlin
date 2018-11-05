package frc.team5499.frc2018Kotlin.utils

import frc.team5499.frc2018Kotlin.Constants

object Utils {

    const val INCHES_PER_SECOND_PER_ENCODER_TICKS_PER_100MS = 10.0

    fun inchesPerSecondToEncoderTicksPer100Ms(ips: Double): Double {
        return inchesToEncoderTicks(ips) / INCHES_PER_SECOND_PER_ENCODER_TICKS_PER_100MS
    }

    fun encoderTicksPer100MsToInchesPerSecond(eps: Int): Double {
        return encoderTicksPer100MsToInchesPerSecond(eps) * INCHES_PER_SECOND_PER_ENCODER_TICKS_PER_100MS
    }

    fun inchesToEncoderTicks(inches: Double): Int {
        return ((Constants.WHEEL_CIR / Constants.ENCODERS_TICKS_PER_ROTATION) * inches) as Int
    }

    fun encoderTicksToInches(ticks: Int): Double {
        return (Constants.WHEEL_CIR / Constants.ENCODERS_TICKS_PER_ROTATION) * ticks
    }
}