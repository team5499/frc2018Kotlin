package tests.utils

import org.junit.jupiter.api.Test

import frc.team5499.frc2018Kotlin.utils.Utils
import frc.team5499.frc2018Kotlin.Constants

public class UtilTest {
    @Test
    fun test_inches_to_encoder_ticks() {
        val inches = 2.0
        val output = Utils.inchesToEncoderTicks(inches)
        assert(output == (2.0 * Constants.ENCODERS_TICKS_PER_ROTATION / Constants.WHEEL_CIR).toInt())
        println("$output")
    }
}
