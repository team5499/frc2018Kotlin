package frc.team5499.frc2018Kotlin.utils

import java.util.logging.Logger
import java.util.logging.Level
import java.util.logging.FileHandler

class CustomLogger {

    companion object {
        val LOG = Logger.getLogger("global")
        init {
            LOG.level = Level.FINEST
            val handler = FileHandler("~/")
            LOG.addHandler(handler)
        }
    }
}
