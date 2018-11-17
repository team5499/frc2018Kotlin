package frc.team5499.frc2018Kotlin.auto.actions

import frc.team5499.frc2018Kotlin.subsystems.Arm

public class ArmAction(armDirectionSt: ArmAction.ArmDirection, intakeDirectionSt: ArmAction.IntakeDirection, timeout: Double) : Action(timeout) {

    enum class ArmDirection {
        UP,
        DOWN,
        HOLD_UP,
        HOLD_DOWN,
        NONE
    }

    enum class IntakeDirection {
        INTAKE,
        HOLD,
        DROP,
        SPIT,
        NONE
    }

    var armDirection: ArmDirection
    var intakeDirection: IntakeDirection

    init{
        armDirection = armDirectionSt //ArmDirection.NONE
        intakeDirection = intakeDirectionSt //IntakeDirection.NONE
    }

    override public fun start() {
        super.start()
    }

    override public fun update() {
        when(armDirection) {
            ArmDirection.UP -> Arm.setArm(1.0)
            ArmDirection.DOWN -> Arm.setArm(-1.0)
            ArmDirection.HOLD_UP -> Arm.setArm(0.2)
            ArmDirection.HOLD_DOWN -> Arm.setArm(-0.2)
            ArmDirection.NONE -> Arm.stopArm()
        }
        when(intakeDirection    ) {
            IntakeDirection.INTAKE -> Arm.intake()
            IntakeDirection.HOLD -> Arm.hold()
            IntakeDirection.DROP -> Arm.drop()
            IntakeDirection.SPIT -> Arm.spit()
            IntakeDirection.NONE -> Arm.stopIntake()
        }
    }

    override public fun next(): Boolean {
        return super.next()
    }

    override public fun finish() {
        println("finish arm")
        //Arm.getInstance().stop()
    }

    override public fun reset() {
        super.reset()
    }
}