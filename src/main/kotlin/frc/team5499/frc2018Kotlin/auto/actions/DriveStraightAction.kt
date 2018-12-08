package frc.team5499.frc2018Kotlin.auto.actions

import frc.team5499.frc2018Kotlin.subsystems.Drivetrain
import frc.team5499.frc2018Kotlin.Constants

public class DriveStraightAction(timeout: Double, inches: Double) : Action(timeout) {

    private val mInches: Double

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
        if (super.next() ||
            (Math.abs(Drivetrain.positionError) < Constants.PID.ACCEPTABLE_DISTANCE_ERROR &&
            Math.abs(Drivetrain.averageVelocity) < Constants.PID.ACCEPTABLE_VELOCITY_THRESHOLD)) {
            return true
        }
        return false
    }

    public override fun finish() {
        super.finish()
    }
}
