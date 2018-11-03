package frc.team5499.frc2018Kotlin.subsystems

abstract class Subsystem() {

    init {}

    abstract fun update()

    abstract fun stop()

    abstract fun reset()

    open fun broadcastToDashboard() {}
}
