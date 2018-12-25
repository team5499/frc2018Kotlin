package frc.team5499.frc2018Kotlin.utils

data class DriveSignal(val left: Double, val right: Double, val brakeMode: Boolean) : CSVWritable {

    constructor(): this(0.0, 0.0)
    constructor(left: Double, right: Double) : this(left, right, false)

    companion object {
        val NEUTRAL = DriveSignal(0.0, 0.0, false)
        val BRAKE = DriveSignal(0.0, 0.0, true)
    }

    override fun toString(): String {
        return "L: $left - R: $right - Brake Mode: $brakeMode "
    }

    override fun toCSV() = "$left,$right,$brakeMode"
}
