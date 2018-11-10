package tests.utils

import org.junit.jupiter.api.Test

import frc.team5499.frc2018Kotlin.utils.CustomLogger

class CustomLoggerTest {

    @Test
    fun test_logger() {
        CustomLogger.LOG.info("Testing logger")
        CustomLogger.LOG.warning("yeet")
        assert(true)
    }
}
