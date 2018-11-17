package frc.team5499.frc2018Kotlin.auto.actions

import frc.team5499.frc2018Kotlin.auto.routines.Routine
import frc.team5499.frc2018Kotlin.auto.routines.Routine.RoutineTag
import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

public class DriveStraightAction(timeout: Double, inches: Double) : Action(timeout) {
    
    var mInches: Double = inches

    override public fun start() {
        super.start()
        Drivetrain.setPosition(mInches, mInches)
    }

    override public fun update() {
        super.update()
    }

    override public fun next(): Boolean {
        if(super.timedOut()) {
            return true
        }
        return false
    }

    override public fun finish() {
        super.finish()
    }

    override public fun reset() {
        super.reset()
    }
}