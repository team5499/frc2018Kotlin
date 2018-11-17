package frc.team5499.frc2018Kotlin.controllers

import java.util.Arrays

import frc.team5499.frc2018Kotlin.auto.actions.Action
import frc.team5499.frc2018Kotlin.auto.actions.ArmAction
//import frc.team5499.frc2018Kotlin.auto.actions.DrivePathAction
import frc.team5499.frc2018Kotlin.auto.actions.DriveStraightAction
import frc.team5499.frc2018Kotlin.auto.actions.NothingAction
import frc.team5499.frc2018Kotlin.auto.actions.ParallelAction
import frc.team5499.frc2018Kotlin.auto.actions.TurnAction
import frc.team5499.frc2018Kotlin.auto.actions.ArmAction.ArmDirection
import frc.team5499.frc2018Kotlin.auto.actions.ArmAction.IntakeDirection
import frc.team5499.frc2018Kotlin.auto.routines.Routine
//import frc.team5499.frc2018Kotlin.path.Path
import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

import edu.wpi.first.wpilibj.DriverStation

object AutoController : Controller() {

    private var left_one_cube: Routine = Routine("left_one_cube")
    private var right_one_cube: Routine = Routine("right_one_cube")
    private var left_two_cube: Routine = Routine("left_two_cube")
    private var right_two_cube: Routine = Routine("right_two_cube")

    private var baseline: Routine = Routine("baseline")

    private var left_outer_one_cube: Routine = Routine("left_outer_one_cube")
    private var right_outer_one_cube: Routine = Routine("right_outer_one_cube")

    private var current_routine: Routine
    private var current_action: Action

    private var is_finished: Boolean

    init {
        is_finished = false
        current_routine = baseline
    }

    public enum class AutoMode {
        LEFT,
        ONE_CENTER,
        TWO_CENTER,
        RIGHT,
        BASELINE
    }

    private var m_mode: AutoMode = AutoMode.ONE_CENTER
    private fun AutoController(){
        //left_one_cube.addAction(DrivePathAction(Path("/home/lvuser/paths/center_to_left.csv", true), 10))
        left_one_cube.addAction(ArmAction(ArmDirection.HOLD_UP, IntakeDirection.DROP, 0.5))
        
        //right_one_cube.addAction(DrivePathAction(Path("/home/lvuser/paths/center_to_right.csv", true), 10))
        right_one_cube.addAction(ArmAction(ArmDirection.HOLD_UP, IntakeDirection.DROP, 0.5))

        //left_two_cube.addAction(DrivePathAction(Path("/home/lvuser/paths/center_to_left.csv", true), 10))
        left_two_cube.addAction(ArmAction(ArmDirection.HOLD_UP, IntakeDirection.DROP, 0.3))
        left_two_cube.addAction(ArmAction(ArmDirection.HOLD_DOWN, IntakeDirection.NONE, 0.7))
        //left_two_cube.addAction(DrivePathAction(Path("/home/lvuser/paths/left_to_pyramid.csv", false) , 10))
        //left_two_cube.addAction(DrivePathAction(Path("/home/lvuser/paths/pyramid_to_left.csv", true), 10))
        
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
        //outtake

        //right_two_cube.addAction(DrivePathAction(Path("/home/lvuser/paths/center_to_right.csv", true), 10))
        right_two_cube.addAction(ArmAction(ArmDirection.HOLD_UP, IntakeDirection.DROP, 0.5))
        right_two_cube.addAction(ArmAction(ArmDirection.DOWN, IntakeDirection.NONE, 0.5))
        right_two_cube.addAction(ParallelAction(5.0, mutableListOf<Action>(
            //DrivePathAction(Path("/home/lvuser/paths/right_to_pyramind.csv", false) , 5),
            ArmAction(ArmDirection.NONE, IntakeDirection.INTAKE, 5.0)
        ))) // arm down, drive path to pyramid, intake
        right_two_cube.addAction(ParallelAction(5.0, mutableListOf<Action>(
            ArmAction(ArmDirection.HOLD_UP, IntakeDirection.HOLD, 2.0)
            //DrivePathAction(Path("/home/lvuser/paths/pyramid_to_right.csv", true), 5)
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

    public fun rotateAuto() {
        when(m_mode) {
            AutoMode.TWO_CENTER -> {
                m_mode = AutoMode.ONE_CENTER
                println("AUTO MODE: Selected One Cube Center Auto!")
            }
            
            AutoMode.ONE_CENTER -> {
                m_mode = AutoMode.RIGHT
                println("AUTO MODE: Selected Right Auto!")
            }
               
            AutoMode.RIGHT -> {
                m_mode = AutoMode.LEFT
                println("AUTO MODE: Selected Left Auto!")  
            }
 
            AutoMode.LEFT -> {
                m_mode = AutoMode.BASELINE
                println("AUTO MODE: Selected Baseline Auto!")
                
            }

            AutoMode.BASELINE -> {
                m_mode = AutoMode.TWO_CENTER
                println("AUTO MODE: Selected Two Cube Center Auto!") 
            }

            else -> {
                m_mode = AutoMode.BASELINE
                println("AUTO MODE: Enum Not Recognised. Baseline Selected!")          
            }
        }
    }
    
    init {
        Drivetrain.reset()
        var is_left: Boolean = DriverStation.getInstance().getGameSpecificMessage().substring(0, 1).equals("L")
        when(m_mode) {
            AutoMode.ONE_CENTER -> {
                if(is_left) {
                    current_routine = left_one_cube
                } else {
                    current_routine = right_one_cube
                }
            }
                
            AutoMode.TWO_CENTER -> {
                if (is_left) {
                    current_routine = left_two_cube
                } else {
                    current_routine = right_two_cube
                }
            }
                
            AutoMode.LEFT -> {
                if (is_left) {
                    current_routine = left_outer_one_cube
                } else {
                    current_routine = baseline
                }
            }
                
            AutoMode.RIGHT -> {
                if (is_left) {
                    current_routine = right_outer_one_cube
                } else {
                    current_routine = baseline
                }
            }   
            AutoMode.BASELINE -> {}

           else -> current_routine = baseline   
        }

        current_action = current_routine.getCurrentAction()
        current_action.start()
    }

    public fun handle() {
        if(is_finished) {
            return
        }
        if(current_routine.isLastStep() && current_action.next()) {
            current_action.finish()
            return
        }
        if(current_action == null){ 
            current_action = current_routine.getCurrentAction()
            current_action.start()
        } else if(current_action.next()) {
            current_action.finish()
            current_routine.advanceRoutine()
            current_action = current_routine.getCurrentAction()
            current_action.start()
        } else {
            current_action.update()
        }
    }

    override fun start(){}

    override fun update(){}

    public override fun reset() {
        baseline.reset()
        left_one_cube.reset()
        right_one_cube.reset()
        left_outer_one_cube.reset()
        right_outer_one_cube.reset()
        left_two_cube.reset()
        right_two_cube.reset()
    }

    //private _instance: AutoController = AutoController()
}