package tests.utils

import frc.team5499.frc2018Kotlin.utils.DriveSignal
import frc.team5499.frc2018Kotlin.utils.Utils
import frc.team5499.frc2018Kotlin.Constants

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

public class UtilTest {
    @Test
    fun test_inches_to_encoder_ticks() {
        val inches = 2.0
        val output = Utils.inchesToEncoderTicks(inches)
        assert(output == (2.0 * Constants.ENCODER_TICKS_PER_ROTATION / Constants.WHEEL_CIR).toInt())
    }

    @Test
    fun funTestDriveSignal() {
        var signal = DriveSignal.NEUTRAL
        assertEquals(signal.left, 0.0, Constants.EPSILON)
        assertEquals(signal.right, 0.0, Constants.EPSILON)
        assert(signal.brakeMode == false)

        signal = DriveSignal.BRAKE
        assertEquals(signal.left, 0.0, Constants.EPSILON)
        assertEquals(signal.right, 0.0, Constants.EPSILON)
        assert(signal.brakeMode == true)

        signal = DriveSignal(0.973, -0.254)
        assertEquals(signal.left, 0.973, Constants.EPSILON)
        assertEquals(signal.right, -0.254, Constants.EPSILON)
        assert(signal.brakeMode == false)
    }
}
