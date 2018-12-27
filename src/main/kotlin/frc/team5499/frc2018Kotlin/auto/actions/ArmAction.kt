package frc.team5499.frc2018Kotlin.auto.actions

import frc.team5499.frc2018Kotlin.subsystems.Arm
import frc.team5499.frc2018Kotlin.Constants

import org.team5499.monkeyLib.auto.Action

public class ArmAction(
    armDirectionSt: ArmAction.ArmDirection,
    intakeDirectionSt: ArmAction.IntakeDirection,
    continueRunning: Boolean = false,
    timeout: Double
) : Action(timeout) {

    enum class ArmDirection(val speed: Double) {
        UP(Constants.Arm.ARM_UP_SPEED),
        DOWN(Constants.Arm.ARM_DOWN_SPEED),
        HOLD_UP(Constants.Arm.ARM_UP_HOLD_SPEED),
        HOLD_DOWN(Constants.Arm.ARM_DOWN_HOLD_SPEED),
        NONE(0.0)
    }

    enum class IntakeDirection(val speed: Double) {
        INTAKE(Constants.Arm.INTAKE_SPEED),
        HOLD(Constants.Arm.INTAKE_HOLD_SPEED),
        DROP(Constants.Arm.INTAKE_DROP_SPEED),
        SPIT(Constants.Arm.INTAKE_SPIT_SPEED),
        NONE(0.0)
    }

    val armDirection: ArmDirection
    val intakeDirection: IntakeDirection
    val mContinue: Boolean

    init {
        armDirection = armDirectionSt
        intakeDirection = intakeDirectionSt
        mContinue = continueRunning
    }

    public override fun start() {
        super.start()
        Arm.setArm(armDirection.speed)
        Arm.setIntake(intakeDirection.speed)
    }

    public override fun update() {}

    public override fun next(): Boolean {
        return super.next()
    }

    public override fun finish() {
        if (!mContinue) {
            Arm.setArm(0.0)
            Arm.setIntake(0.0)
        }
    }
}
