package frc.team5499.frc2018Kotlin.auto.actions

public class ArmAction(armDirectionSt: ArmDirection, intakeDirectionSt: IntakeDirection, val timeout: Double) : Action(timeout) {

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
    init{
        var armDirection: ArmDirection = armDirectionSt //ArmDirection.NONE
        var intakeDirection: IntakeDirection = intakeDirectionSt//IntakeDirection.NONE
    }
    
    /*constructor(armDirection: ArmDirection, intakeDirection: IntakeDirection): this(timeout) {
        this.armDirection = armDirection
        this.intakeDirection = intakeDirection
    }*/

    override public fun start() {
        super.start()
    }

    override public fun update() {

        /* when(arm_direction) {
            ArmDirection.UP -> Arm.getInstance().setArm(1.0)
            ArmDirection.DOWN -> Arm.getInstance().setArm(-1.0)
            ArmDirection.HOLD_UP -> Arm.getInstance().setArm(0.2)
            ArmDirection.HOLD_DOWN -> Arm.getInstance().setArm(-0.2)
            ArmDirection.NONE -> Arm.getInstance().stopArm()
        }
        when(intake_direction) {
            IntakeDirection.INTAKE -> Arm.getInstance().intake()
            IntakeDirection.HOLD -> Arm.getInstance().hold()
            IntakeDirection.DROP -> Arm.getInstance().drop()
            IntakeDirection.SPIT -> Arm.getInstance().spit()
            IntakeDirection.NONE -> // Arm.getInstance().stopIntake()
        }*/
    }

    override public fun next(): Boolean {
        return super.next()
    }

    override public fun finish() {
        System.out.println("finish arm")
        //Arm.getInstance().stop()
    }

    override public fun reset() {
        super.reset()
    }
}