package frc.team5499.frc2018Kotlin.utils

import java.util.logging.Logger
import java.util.logging.Level

import java.io.File

object CustomLogger {
    val LOG = Logger.getLogger("global")

    init {
        LOG.level = Level.FINEST
        println("yeet") // why doesnt this print
        val mediaDir: File = File("/lvuser/")
        println(mediaDir.listFiles())
            // val handler = FileHandler("~/media/log.log")
            // val formatter = SimpleFormatter()
            // handler.setFormatter(formatter)
            // LOG.addHandler(handler)
    }
}
