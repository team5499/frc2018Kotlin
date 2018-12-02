package frc.team5499.frc2018Kotlin.auto.routines

import frc.team5499.frc2018Kotlin.auto.actions.NothingAction

@SuppressWarnings("MagicNumber")
object Routines {

    val centerLeftSwitch: Routine

    val centerRightSwitch: Routine

    val leftOuterSwitch: Routine

    val rightOuterSwitch: Routine

    val baseline: Routine

    init {
        this.centerLeftSwitch = generateCenterLeftSwitchRoutine()
        centerRightSwitch = generateCenterRightSwitchRoutine()
        leftOuterSwitch = leftOuterSwitchRoutine()
        rightOuterSwitch = rightOuterSwitchRoutine()
        baseline = generateBaselineRoutine()
    }

    private fun generateCenterLeftSwitchRoutine(): Routine {
        val r = Routine("center_switch_left")
        r.addAction(NothingAction(10.0))
        return r
    }

    private fun generateCenterRightSwitchRoutine(): Routine {
        val r = Routine("center_switch_right")
        r.addAction(NothingAction(10.0))
        return r
    }

    private fun leftOuterSwitchRoutine(): Routine {
        val r = Routine("left_outer")
        r.addAction(NothingAction(10.0))
        return r
    }

    private fun rightOuterSwitchRoutine(): Routine {
        val r = Routine("right_outer")
        r.addAction(NothingAction(10.0))
        return r
    }

    private fun generateBaselineRoutine(): Routine {
        val r = Routine("baseline")
        r.addAction(NothingAction(10.0))
        return r
    }
}
