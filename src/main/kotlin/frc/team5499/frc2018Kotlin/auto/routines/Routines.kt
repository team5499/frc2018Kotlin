package frc.team5499.frc2018Kotlin.auto.routines

import frc.team5499.frc2018Kotlin.path.PathGenerator

import frc.team5499.frc2018Kotlin.auto.actions.NothingAction
import frc.team5499.frc2018Kotlin.auto.actions.DrivePathAction

@SuppressWarnings("MagicNumber")
object Routines {

    val centerLeftSwitch: Routine

    val centerRightSwitch: Routine

    val leftOuterSwitch: Routine

    val rightOuterSwitch: Routine

    val baseline: Routine

    init {
        centerLeftSwitch = generateCenterLeftSwitchRoutine()
        centerRightSwitch = generateCenterRightSwitchRoutine()
        leftOuterSwitch = leftOuterSwitchRoutine()
        rightOuterSwitch = rightOuterSwitchRoutine()
        baseline = generateBaselineRoutine()
    }

    private fun generateCenterLeftSwitchRoutine(): Routine {
        val r = Routine("center_switch_left", 180.0,
            DrivePathAction(PathGenerator.Paths.startLeftSwitch, 100.0),
            NothingAction(1.0),
            DrivePathAction(PathGenerator.Paths.leftSwitchToPyramid, 100.0),
            NothingAction(1.0),
            DrivePathAction(PathGenerator.Paths.pyramidToLeftSwitch, 100.0)
        )
        return r
    }

    private fun generateCenterRightSwitchRoutine(): Routine {
        val r = Routine("center_switch_right", 180.0,
            DrivePathAction(PathGenerator.Paths.startRightSwitch, 100.0),
            NothingAction(1.0),
            DrivePathAction(PathGenerator.Paths.rightSwitchToPyramid, 100.0),
            NothingAction(1.0),
            DrivePathAction(PathGenerator.Paths.pyramidToRightSwitch, 100.0)
        )
        return r
    }

    private fun leftOuterSwitchRoutine(): Routine {
        val r = Routine("left_outer", 180.0,
            NothingAction(10.0)
        )
        return r
    }

    private fun rightOuterSwitchRoutine(): Routine {
        val r = Routine("right_outer", 180.0,
            NothingAction(10.0)
        )
        return r
    }

    private fun generateBaselineRoutine(): Routine {
        val r = Routine("baseline", 180.0,
            NothingAction(10.0)
        )
        return r
    }

    public fun resetAll() {
        centerLeftSwitch.reset()
        centerRightSwitch.reset()
        leftOuterSwitch.reset()
        rightOuterSwitch.reset()
        baseline.reset()
    }
}
