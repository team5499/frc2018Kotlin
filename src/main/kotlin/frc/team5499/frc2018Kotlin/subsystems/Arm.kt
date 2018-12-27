package frc.team5499.frc2018Kotlin.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode

import frc.team5499.frc2018Kotlin.Constants

import org.team5499.monkeyLib.Subsystem
import org.team5499.monkeyLib.hardware.LazyTalonSRX

@Suppress("TooManyFunctions")
object Arm : Subsystem() {
    private val mArm: LazyTalonSRX = LazyTalonSRX(Constants.Talons.ARM_PORT).apply {
        setInverted(Constants.Arm.INVERT_ARM)
    }

    private val mLeftIntake: LazyTalonSRX = LazyTalonSRX(Constants.Talons.LEFT_INTAKE_PORT)

    private val mRightIntake: LazyTalonSRX = LazyTalonSRX(Constants.Talons.RIGHT_INTAKE_PORT)

    init {
        mLeftIntake.setInverted(Constants.Arm.INVERT_INTAKE)
        mRightIntake.setInverted(Constants.Arm.INVERT_INTAKE)
    }

    override fun update() {}

    override fun stop() {
        setArm(0.0)
        stopIntake()
    }

    override fun reset() {
        mLeftIntake.setNeutralMode(NeutralMode.Coast)
        mRightIntake.setNeutralMode(NeutralMode.Coast)
    }

    fun setArm(sp: Double) {
        var speed: Double = sp
        if (Math.abs(speed) > Constants.Arm.MAX_ARM_SPEED) {
            speed = Math.signum(speed) * Constants.Arm.MAX_ARM_SPEED
        }
        mArm.set(ControlMode.PercentOutput, speed)
    }

    fun setIntake(speed: Double) {
        mLeftIntake.setNeutralMode(NeutralMode.Brake)
        mRightIntake.setNeutralMode(NeutralMode.Brake)
        mLeftIntake.set(ControlMode.PercentOutput, speed)
        mRightIntake.set(ControlMode.PercentOutput, speed)
    }

    fun intake() {
        mLeftIntake.setNeutralMode(NeutralMode.Brake)
        mRightIntake.setNeutralMode(NeutralMode.Brake)
        mLeftIntake.set(ControlMode.PercentOutput, Constants.Arm.INTAKE_SPEED)
        mRightIntake.set(ControlMode.PercentOutput, Constants.Arm.INTAKE_SPEED)
    }

    fun spit() {
        mLeftIntake.setNeutralMode(NeutralMode.Brake)
        mRightIntake.setNeutralMode(NeutralMode.Brake)
        mLeftIntake.set(ControlMode.PercentOutput, Constants.Arm.INTAKE_SPIT_SPEED)
        mRightIntake.set(ControlMode.PercentOutput, Constants.Arm.INTAKE_SPIT_SPEED)
    }

    fun hold() {
        mLeftIntake.setNeutralMode(NeutralMode.Brake)
        mRightIntake.setNeutralMode(NeutralMode.Brake)
        mLeftIntake.set(ControlMode.PercentOutput, Constants.Arm.INTAKE_HOLD_SPEED)
        mRightIntake.set(ControlMode.PercentOutput, Constants.Arm.INTAKE_HOLD_SPEED)
    }

    fun stopIntake() {
        mLeftIntake.setNeutralMode(NeutralMode.Brake)
        mRightIntake.setNeutralMode(NeutralMode.Brake)
        mLeftIntake.set(ControlMode.PercentOutput, 0.0)
        mRightIntake.set(ControlMode.PercentOutput, 0.0)
    }

    fun drop() {
        mLeftIntake.setNeutralMode(NeutralMode.Brake)
        mRightIntake.setNeutralMode(NeutralMode.Brake)
        mLeftIntake.set(ControlMode.PercentOutput, Constants.Arm.INTAKE_DROP_SPEED)
        mRightIntake.set(ControlMode.PercentOutput, Constants.Arm.INTAKE_DROP_SPEED)
    }

    fun stopArm() {
        System.out.println("stop")
        mArm.set(ControlMode.PercentOutput, 0.0)
    }
}
