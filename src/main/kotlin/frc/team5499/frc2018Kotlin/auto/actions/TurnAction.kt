package frc.team5499.frc2018Kotlin.auto.actions

import frc.team5499.frc2018Kotlin.auto.routines.Routine
import frc.team5499.frc2018Kotlin.auto.routines.Routine.RoutineTag
import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

public class TurnAction(timeout: Double, angle: Double) : Action(timeout) { //ADD 'TYPE: TURNTYPE' WHEN ABOSUTLE TURN IS INITIALIZED

    public enum class TurnType{
        ABOSLUTE,
        RELATIVE
    }

    var mAngle: Double = angle

    init{
        var mAngle: Double = angle
        //var mType: TurnType = type
    }

    override public fun start() {
        super.start()
        Drivetrain.setTurn(mAngle)
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

    override public fun reset() {
        super.reset()
    }
}