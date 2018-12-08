package frc.team5499.frc2018Kotlin.auto.actions

import frc.team5499.frc2018Kotlin.subsystems.Arm
import frc.team5499.frc2018Kotlin.Constants

public class ArmAction(
    armDirectionSt: ArmAction.ArmDirection,
    intakeDirectionSt: ArmAction.IntakeDirection,
    waitForTimeout: Boolean = false,
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
        DROP(Constants.Arm.INTAKE_HOLD_SPEED),
        SPIT(Constants.Arm.INTAKE_SPIT_SPEED),
        NONE(0.0)
    }

    val armDirection: ArmDirection
    val intakeDirection: IntakeDirection
    val mWaitForTimeout: Boolean

    init {
        armDirection = armDirectionSt
        intakeDirection = intakeDirectionSt
        mWaitForTimeout = waitForTimeout
    }

    public override fun start() {
        super.start()
        Arm.setArm(armDirection.speed)
        Arm.setIntake(intakeDirection.speed)
        Arm.setArm(armDirection.speed)
    }

    public override fun update() {}

    public override fun next(): Boolean {
        if (mWaitForTimeout)
            return super.next()
        else
            return true
    }
}
