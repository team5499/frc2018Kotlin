package frc.team5499.frc2018Kotlin.utils

import java.util.logging.Logger
import java.util.logging.Level
import java.util.logging.FileHandler
import java.util.logging.SimpleFormatter

class CustomLogger {

    companion object {
        val LOG = Logger.getLogger("global")
        init {
            LOG.level = Level.FINEST
            val handler = FileHandler("log.log")
            val formatter = SimpleFormatter()
            handler.setFormatter(formatter)
            LOG.addHandler(handler)
        }
    }
}
