package frc.team5499.frc2018Kotlin.controllers

import frc.team5499.frc2018Kotlin.auto.routines.Routines
import frc.team5499.frc2018Kotlin.subsystems.Drivetrain
import frc.team5499.frc2018Kotlin.subsystems.Arm

import org.team5499.monkeyLib.Controller
import org.team5499.monkeyLib.auto.Routine
import org.team5499.monkeyLib.auto.Action
import org.team5499.monkeyLib.math.geometry.Rotation2d

import edu.wpi.first.wpilibj.DriverStation

@SuppressWarnings("MagicNumber")
object AutoController : Controller() {

    private var currentRoutine: Routine
    private var currentAction: Action?

    private var isFinished: Boolean

    public enum class AutoMode {
        CENTER,
        LEFT,
        RIGHT,
        BASELINE
    }

    private var mMode: AutoMode

    init {
        isFinished = false
        currentRoutine = Routines.baseline
        currentAction = null
        mMode = AutoMode.BASELINE
        println("AUTO MODE: Selected Baseline Auto!")
    }

    public fun rotateAuto() {
        when (mMode) {
            AutoMode.CENTER -> {
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
                mMode = AutoMode.CENTER
                println("AUTO MODE: Selected Center Auto!")
            }
        }
    }

    @Suppress("ComplexMethod")
    override fun start() {
        reset()
        var is_left: Boolean = DriverStation.getInstance().getGameSpecificMessage().substring(0, 1).equals("L")
        when (mMode) {
            AutoMode.CENTER -> {
                if (is_left) {
                    currentRoutine = Routines.centerLeftSwitch
                } else {
                    currentRoutine = Routines.centerRightSwitch
                }
            }

            AutoMode.LEFT -> {
                if (is_left) {
                    currentRoutine = Routines.leftOuterSwitch
                } else {
                    currentRoutine = Routines.baseline
                }
            }

            AutoMode.RIGHT -> {
                if (is_left) {
                    currentRoutine = Routines.rightOuterSwitch
                } else {
                    currentRoutine = Routines.baseline
                }
            }

            AutoMode.BASELINE -> {
                currentRoutine = Routines.baseline
            }
        }
        Drivetrain.heading = Rotation2d(currentRoutine.startHeading)
        currentAction = currentRoutine.getCurrentAction()
        currentAction!!.start()
    }

    override fun update() {
        if (isFinished) {
            return
        }
        if (currentRoutine.isLastStep() && currentAction!!.next()) {
            currentAction!!.finish()
            isFinished = true
            return
        }
        if (currentAction == null) {
            currentAction = currentRoutine.getCurrentAction()
            currentAction!!.start()
        } else if (currentAction!!.next()) {
            currentAction!!.finish()
            currentRoutine.advanceRoutine()
            currentAction = currentRoutine.getCurrentAction()
            currentAction!!.start()
        } else {
            currentAction!!.update()
        }
    }

    public override fun reset() {
        currentAction = null
        currentRoutine = Routines.baseline
        Routines.resetAll()
        Drivetrain.reset()
        Arm.reset()
        isFinished = false
    }
}
