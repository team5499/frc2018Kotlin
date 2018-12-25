package frc.team5499.frc2018Kotlin.utils

import java.io.FileNotFoundException
import java.io.PrintWriter
import java.lang.reflect.Field
import java.util.concurrent.ConcurrentLinkedDeque

class ReflectingCSVWriter<T>(filename: String, typeClass: Class<T>) {
    private val mLinesToWrite: ConcurrentLinkedDeque<String> = ConcurrentLinkedDeque()
    private var mOutput: PrintWriter? = null
    private val mFields: Array<Field>

    init {
        mFields = typeClass.getFields()
        try {
            mOutput = PrintWriter(filename)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        val line = StringBuffer()
        for (f in mFields) {
            if (line.length != 0) {
                line.append(", ")
            }
            line.append(f.getName())
        }
        writeLine(line.toString())
    }

    public fun add(value: T) {
        val line: StringBuffer = StringBuffer()
        for (f in mFields) {
            if (line.length != 0) {
                line.append(", ")
            }
            try {
                if (CSVWritable::class.java.isAssignableFrom(f.getType())) {
                    line.append((f.get(value) as CSVWritable).toCSV())
                } else {
                    line.append(f.get(value).toString())
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: IllegalAccessError) {
                e.printStackTrace()
            }
        }
    }

    @Synchronized protected fun writeLine(line: String) {
        if (mOutput != null) {
            mOutput?.println(line)
        }
    }

    public fun write() {
        while (true) {
            val v = mLinesToWrite.pollFirst()
            if (v == null) {
                break
            }
            writeLine(v)
        }
    }

    @Synchronized public fun flush() {
        if (mOutput != null) {
            write()
            mOutput?.flush()
        }
    }
}
