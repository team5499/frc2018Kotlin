package frc.team5499.frc2018Kotlin.auto.actions

import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

class DrivePathAction(timeout: Double, inches: Double) : Action(timeout) {

    var mInches: Double

    init {
        mInches = inches
    }

    override fun start() {
        super.start()
        Drivetrain.setPosition(mInches)
    }

    override fun update() {
        super.update()
    }

    override fun next(): Boolean {
        if (super.timedOut()) {
            return true
        }
        return false
    }

    override fun finish() {
        super.finish()
    }

    override fun reset() {
        super.reset()
    }
}
