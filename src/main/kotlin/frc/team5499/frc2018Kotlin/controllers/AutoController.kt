package frc.team5499.frc2018Kotlin.controllers

import frc.team5499.frc2018Kotlin.auto.actions.Action
import frc.team5499.frc2018Kotlin.auto.actions.ArmAction
// import frc.team5499.frc2018Kotlin.auto.actions.DrivePathAction
import frc.team5499.frc2018Kotlin.auto.actions.DriveStraightAction
import frc.team5499.frc2018Kotlin.auto.actions.NothingAction
import frc.team5499.frc2018Kotlin.auto.actions.ParallelAction
import frc.team5499.frc2018Kotlin.auto.actions.TurnAction
import frc.team5499.frc2018Kotlin.auto.actions.ArmAction.ArmDirection
import frc.team5499.frc2018Kotlin.auto.actions.ArmAction.IntakeDirection
import frc.team5499.frc2018Kotlin.auto.routines.Routine
// import frc.team5499.frc2018Kotlin.path.Path
import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

import edu.wpi.first.wpilibj.DriverStation

@SuppressWarnings("MagicNumber", "ObjectPropertyNaming")
object AutoController : Controller() {

    private var left_one_cube: Routine = Routine("left_one_cube")
    private var right_one_cube: Routine = Routine("right_one_cube")
    private var left_two_cube: Routine = Routine("left_two_cube")
    private var right_two_cube: Routine = Routine("right_two_cube")

    private var baseline: Routine = Routine("baseline")

    private var left_outer_one_cube: Routine = Routine("left_outer_one_cube")
    private var right_outer_one_cube: Routine = Routine("right_outer_one_cube")

    private var currentRoutine: Routine
    private var currentAction: Action

    private var isFinished: Boolean

    init {
        isFinished = false
        currentRoutine = baseline
        // left_one_cube.addAction(DrivePathAction(Path("/home/lvuser/paths/center_to_left.csv", true), 10))
        left_one_cube.addAction(ArmAction(ArmDirection.HOLD_UP, IntakeDirection.DROP, 0.5))

        // right_one_cube.addAction(DrivePathAction(Path("/home/lvuser/paths/center_to_right.csv", true), 10))
        right_one_cube.addAction(ArmAction(ArmDirection.HOLD_UP, IntakeDirection.DROP, 0.5))

        // left_two_cube.addAction(DrivePathAction(Path("/home/lvuser/paths/center_to_left.csv", true), 10))
        left_two_cube.addAction(ArmAction(ArmDirection.HOLD_UP, IntakeDirection.DROP, 0.3))
        left_two_cube.addAction(ArmAction(ArmDirection.HOLD_DOWN, IntakeDirection.NONE, 0.7))
        // left_two_cube.addAction(DrivePathAction(Path("/home/lvuser/paths/left_to_pyramid.csv", false) , 10))
        // left_two_cube.addAction(DrivePathAction(Path("/home/lvuser/paths/pyramid_to_left.csv", true), 10))

        /*left_two_cube.addAction(ParallelAction(5, Arrays.asList(
            DrivePathAction(Path("home/lvuser/paths/left_to_pyramid.csv", false) , 5),
            ArmAction(ArmDirection.NONE, IntakeDirection.INTAKE, 5)
        ))) // arm down, drive path to pyramid, intake
        */
        /*
        left_two_cube.addAction(ParallelAction(5, Arrays.asList(
            ArmAction(ArmDirection.HOLD_UP, IntakeDirection.HOLD, 2),
            DrivePathAction(Path("home/lvuser/paths/pyramid_to_left.csv", true), 5)
        ))) // arm up, drive back to switch
        */
        left_two_cube.addAction(ArmAction(ArmDirection.HOLD_UP, IntakeDirection.DROP, 0.5))
        // outtake

        // right_two_cube.addAction(DrivePathAction(Path("/home/lvuser/paths/center_to_right.csv", true), 10))
        right_two_cube.addAction(ArmAction(ArmDirection.HOLD_UP, IntakeDirection.DROP, 0.5))
        right_two_cube.addAction(ArmAction(ArmDirection.DOWN, IntakeDirection.NONE, 0.5))
        right_two_cube.addAction(ParallelAction(5.0, mutableListOf<Action>(
            // DrivePathAction(Path("/home/lvuser/paths/right_to_pyramind.csv", false) , 5),
            ArmAction(ArmDirection.NONE, IntakeDirection.INTAKE, 5.0)
        ))) // arm down, drive path to pyramid, intake
        right_two_cube.addAction(ParallelAction(5.0, mutableListOf<Action>(
            ArmAction(ArmDirection.HOLD_UP, IntakeDirection.HOLD, 2.0)
            // DrivePathAction(Path("/home/lvuser/paths/pyramid_to_right.csv", true), 5)
        ))) // arm up, drive back to switch
        right_two_cube.addAction(ArmAction(ArmDirection.HOLD_UP, IntakeDirection.DROP, 0.5))

        baseline.addAction(ArmAction(ArmDirection.UP, IntakeDirection.HOLD, 0.2))
        baseline.addAction(DriveStraightAction(3.0, -106.0))

        right_outer_one_cube.addAction(NothingAction(0.0))
        right_outer_one_cube.addAction(ArmAction(ArmDirection.UP, IntakeDirection.HOLD, 0.4))
        right_outer_one_cube.addAction(DriveStraightAction(3.0, -150.0))
        right_outer_one_cube.addAction(TurnAction(2.0, 90.0))
        right_outer_one_cube.addAction(DriveStraightAction(1.5, -35.0))
        right_outer_one_cube.addAction(ArmAction(ArmDirection.NONE, IntakeDirection.DROP, 0.4))

        left_outer_one_cube.addAction(NothingAction(0.0))
        left_outer_one_cube.addAction(ArmAction(ArmDirection.UP, IntakeDirection.HOLD, 0.4))
        left_outer_one_cube.addAction(DriveStraightAction(3.0, -150.0))
        left_outer_one_cube.addAction(TurnAction(1.5, -90.0))
        left_outer_one_cube.addAction(DriveStraightAction(1.5, -35.0))
        left_outer_one_cube.addAction(ArmAction(ArmDirection.NONE, IntakeDirection.DROP, 0.4))

        println("AUTO MODE: Selected Center Auto!")
        println("CUBE MODE: One Cube!")
    }

    public enum class AutoMode {
        LEFT,
        ONE_CENTER,
        TWO_CENTER,
        RIGHT,
        BASELINE
    }

    private var mMode: AutoMode = AutoMode.ONE_CENTER

    public fun rotateAuto() {
        when (mMode) {
            AutoMode.TWO_CENTER -> {
                mMode = AutoMode.ONE_CENTER
                println("AUTO MODE: Selected One Cube Center Auto!")
            }

            AutoMode.ONE_CENTER -> {
                mMode = AutoMode.RIGHT
                println("AUTO MODE: Selected Right Auto!")
            }

            AutoMode.RIGHT -> {
                mMode = AutoMode.LEFT
                println("AUTO MODE: Selected Left Auto!")
            }

            AutoMode.LEFT -> {
                mMode = AutoMode.BASELINE
                println("AUTO MODE: Selected Baseline Auto!")
            }

            AutoMode.BASELINE -> {
                mMode = AutoMode.TWO_CENTER
                println("AUTO MODE: Selected Two Cube Center Auto!")
            }
        }
    }

    init {
        Drivetrain.reset()
        var is_left: Boolean = DriverStation.getInstance().getGameSpecificMessage().substring(0, 1).equals("L")
        when (mMode) {
            AutoMode.ONE_CENTER -> {
                if (is_left) {
                    currentRoutine = left_one_cube
                } else {
                    currentRoutine = right_one_cube
                }
            }

            AutoMode.TWO_CENTER -> {
                if (is_left) {
                    currentRoutine = left_two_cube
                } else {
                    currentRoutine = right_two_cube
                }
            }

            AutoMode.LEFT -> {
                if (is_left) {
                    currentRoutine = left_outer_one_cube
                } else {
                    currentRoutine = baseline
                }
            }

            AutoMode.RIGHT -> {
                if (is_left) {
                    currentRoutine = right_outer_one_cube
                } else {
                    currentRoutine = baseline
                }
            }
            AutoMode.BASELINE -> {}
        }

        currentAction = currentRoutine.getCurrentAction()
        currentAction.start()
    }

    public fun handle() {
        if (isFinished) {
            return
        }
        if (currentRoutine.isLastStep() && currentAction.next()) {
            currentAction.finish()
            return
        }
        if (currentAction == null) {
            currentAction = currentRoutine.getCurrentAction()
            currentAction.start()
        } else if (currentAction.next()) {
            currentAction.finish()
            currentRoutine.advanceRoutine()
            currentAction = currentRoutine.getCurrentAction()
            currentAction.start()
        } else {
            currentAction.update()
        }
    }

    override fun start() {}

    override fun update() {}

    public override fun reset() {
        baseline.reset()
        left_one_cube.reset()
        right_one_cube.reset()
        left_outer_one_cube.reset()
        right_outer_one_cube.reset()
        left_two_cube.reset()
        right_two_cube.reset()
    }

    // private _instance: AutoController = AutoController()
}
