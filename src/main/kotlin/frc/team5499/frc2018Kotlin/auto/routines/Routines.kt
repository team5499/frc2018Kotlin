package frc.team5499.frc2018Kotlin.auto.routines

import frc.team5499.frc2018Kotlin.path.PathGenerator

import frc.team5499.frc2018Kotlin.auto.actions.NothingAction
import frc.team5499.frc2018Kotlin.auto.actions.DrivePathAction
import frc.team5499.frc2018Kotlin.auto.actions.WaitUntilRegionAction
import frc.team5499.frc2018Kotlin.auto.actions.CrossedXBoundaryAction
import frc.team5499.frc2018Kotlin.auto.actions.ArmAction
import frc.team5499.frc2018Kotlin.auto.actions.ParallelAction
import frc.team5499.frc2018Kotlin.auto.actions.SerialAction
import frc.team5499.frc2018Kotlin.auto.actions.ArmAction.IntakeDirection
import frc.team5499.frc2018Kotlin.auto.actions.ArmAction.ArmDirection

import frc.team5499.frc2018Kotlin.utils.math.geometry.Vector2

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
            ParallelAction(
                DrivePathAction(PathGenerator.Paths.startLeftSwitch, 100.0),
                SerialAction(
                    CrossedXBoundaryAction(90.0, false),
                    ArmAction(ArmDirection.NONE, IntakeDirection.DROP, true, 0.75)
                )
            ),
            ParallelAction(
                DrivePathAction(PathGenerator.Paths.leftSwitchToPyramid, 100.0),
                SerialAction(
                    ArmAction(ArmDirection.DOWN, IntakeDirection.NONE, true, 0.5),
                    ArmAction(ArmDirection.NONE, IntakeDirection.INTAKE, true, 0.0)
                )
            ),
            ParallelAction(
                DrivePathAction(PathGenerator.Paths.pyramidToLeftSwitch, 100.0),
                SerialAction(
                    ArmAction(ArmDirection.UP, IntakeDirection.HOLD, true, 0.7),
                    ArmAction(ArmDirection.NONE, IntakeDirection.NONE, true, 1.0)
                )
            ),
            ArmAction(ArmDirection.NONE, IntakeDirection.DROP, true, 1.0)

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
            WaitUntilRegionAction(Vector2(12.0, 12.0), Vector2(48.0, 40.0)),
            ArmAction(ArmDirection.NONE, IntakeDirection.DROP, true, 2.0),
            ArmAction(ArmDirection.NONE, IntakeDirection.NONE, false, 0.0)
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
