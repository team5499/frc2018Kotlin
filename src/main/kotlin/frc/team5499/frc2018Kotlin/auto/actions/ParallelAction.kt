package frc.team5499.frc2018Kotlin.auto.actions

import frc.team5499.frc2018Kotlin.auto.routines.Routine
import frc.team5499.frc2018Kotlin.auto.routines.Routine.RoutineTag

public class ParallelAction(timeout: Double, actions: MutableList<Action>) : Action(timeout) {

    var mActions: MutableList<Action> = actions

    override public fun start() {
        super.start()
        for (a: Action in mActions) {
            a.start()
        }
    }

    override public fun update() {
        super.update()
        for (a: Action in mActions) {
            a.start()
        }
    }

    override public fun next(): Boolean {
        if(super.timedOut()) {
            return true
        }
        for(a: Action in mActions) {
            if(!a.next()) {
                return false
            }
        }
        return true
    }

    override public fun finish() {
        super.finish()
        for (a: Action in mActions) {
            a.finish()
        }
    }

    override public fun reset() {
        super.reset()
        for (a: Action in mActions) {
            a.reset()
        }
    }
}