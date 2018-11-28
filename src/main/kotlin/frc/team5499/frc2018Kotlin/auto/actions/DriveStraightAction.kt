package frc.team5499.frc2018Kotlin.auto.actions

import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

public class DriveStraightAction(timeout: Double, inches: Double) : Action(timeout) {

    var mInches: Double

    init {
        mInches = inches
    }

    public override fun start() {
        super.start()
        Drivetrain.setPosition(mInches)
    }

    public override fun update() {
        super.update()
    }

    public override fun next(): Boolean {
        if (super.timedOut() || Drivetrain.positionError < 2.0) {
            return true
        }
        return false
    }

    public override fun finish() {
        super.finish()
    }

    public override fun reset() {
        super.reset()
    }
}
