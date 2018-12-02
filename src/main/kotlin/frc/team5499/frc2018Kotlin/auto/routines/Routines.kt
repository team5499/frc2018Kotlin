package frc.team5499.frc2018Kotlin.auto.routines

import frc.team5499.frc2018Kotlin.auto.actions.NothingAction

@SuppressWarnings("MagicNumber")
object Routines {

    var mRoutineSet: RoutineSet?

    init {
        mRoutineSet = null
    }

    fun generateRoutineSet() {
        if (mRoutineSet == null) {
            mRoutineSet = RoutineSet()
        }
    }

    class RoutineSet {

        val centerLeftSwitch: Routine
            get() = field
        val centerRightSwitch: Routine
            get() = field
        val leftOuterSwitch: Routine
            get() = field
        val rightOuterSwitch: Routine
            get() = field
        val baseline: Routine
            get() = field

        init {
            centerLeftSwitch = generateCenterLeftSwitchRoutine()
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
}
