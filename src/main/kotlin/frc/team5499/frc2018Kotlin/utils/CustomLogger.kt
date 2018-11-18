package frc.team5499.frc2018Kotlin.utils

import java.util.logging.Logger
import java.util.logging.Level

import java.io.File

object CustomLogger {
    var pathToLogsFolder: String = ""
    val LOG = Logger.getLogger("global")

    init {
        LOG.level = Level.FINEST
        val mediaDir: File = File("/media/")
        var list = mediaDir.list()
        // find drive with 'logs' folder on it
        a@ for (i in list.indices) {

            val temp = File("/media/${list[i]}")
            var list2 = temp.list()
            for (j in list2.indices) {
                if (list2[j].equals("logs")) {
                    pathToLogsFolder = "/media/${list[i]}/${list2[j]}/"
                    println("Found logs folder at: $pathToLogsFolder")
                    break@a
                }
            }
        }
        // write to file if file is found
        if (pathToLogsFolder.equals("")) {
            println("No logs folder found!")
        } else {
            // val currentTime = LocalDateTime.now()
            // var fileName = "robot-log-$currentTime.log"
            // val handler = FileHandler(pathToLogsFolder + fileName)
            // val formatter = SimpleFormatter()
            // handler.setFormatter(formatter)
            // LOG.addHandler(handler)
        }
    }
}
