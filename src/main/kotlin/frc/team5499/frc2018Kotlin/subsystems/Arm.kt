package frc.team5499.frc2018Kotlin.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX

import frc.team5499.frc2018Kotlin.Constants

@Suppress("TooManyFunctions")
object Arm : Subsystem() {
    val m_arm: TalonSRX = TalonSRX(Constants.Talons.ARM_PORT).apply {
        setInverted(Constants.Arm.INVERT_ARM)
    }
    val m_left_intake: TalonSRX = TalonSRX(Constants.Talons.LEFT_INTAKE_PORT).apply {
    }
    val m_right_intake: TalonSRX = TalonSRX(Constants.Talons.RIGHT_INTAKE_PORT)

    init {
        m_left_intake.setInverted(Constants.Arm.INVERT_INTAKE)
        m_right_intake.setInverted(Constants.Arm.INVERT_INTAKE)
    }

    override fun update() {}

    override fun stop() {
        setArm(0.0)
        stopIntake()
    }

    override fun reset() {
        m_left_intake.setNeutralMode(NeutralMode.Coast)
        m_right_intake.setNeutralMode(NeutralMode.Coast)
    }

    fun setArm(sp: Double) {
        var speed: Double = sp
        if (Math.abs(speed) > Constants.Arm.MAX_ARM_SPEED) {
            speed = Math.signum(speed) * Constants.Arm.MAX_ARM_SPEED
        }
        m_arm.set(ControlMode.PercentOutput, speed)
    }

    fun setIntake(speed: Double) {
        m_left_intake.setNeutralMode(NeutralMode.Brake)
        m_right_intake.setNeutralMode(NeutralMode.Brake)
        m_left_intake.set(ControlMode.PercentOutput, speed)
        m_right_intake.set(ControlMode.PercentOutput, speed)
    }

    fun intake() {
        m_left_intake.setNeutralMode(NeutralMode.Brake)
        m_right_intake.setNeutralMode(NeutralMode.Brake)
        m_left_intake.set(ControlMode.PercentOutput, Constants.Arm.INTAKE_SPEED)
        m_right_intake.set(ControlMode.PercentOutput, Constants.Arm.INTAKE_SPEED)
    }

    fun spit() {
        m_left_intake.setNeutralMode(NeutralMode.Brake)
        m_right_intake.setNeutralMode(NeutralMode.Brake)
        m_left_intake.set(ControlMode.PercentOutput, Constants.Arm.INTAKE_SPIT_SPEED)
        m_right_intake.set(ControlMode.PercentOutput, Constants.Arm.INTAKE_SPIT_SPEED)
    }

    fun hold() {
        m_left_intake.setNeutralMode(NeutralMode.Brake)
        m_right_intake.setNeutralMode(NeutralMode.Brake)
        m_left_intake.set(ControlMode.PercentOutput, Constants.Arm.INTAKE_HOLD_SPEED)
        m_right_intake.set(ControlMode.PercentOutput, Constants.Arm.INTAKE_HOLD_SPEED)
    }

    fun stopIntake() {
        m_left_intake.setNeutralMode(NeutralMode.Brake)
        m_right_intake.setNeutralMode(NeutralMode.Brake)
        m_left_intake.set(ControlMode.PercentOutput, 0.0)
        m_right_intake.set(ControlMode.PercentOutput, 0.0)
    }

    fun drop() {
        m_left_intake.setNeutralMode(NeutralMode.Brake)
        m_right_intake.setNeutralMode(NeutralMode.Brake)
        m_left_intake.set(ControlMode.PercentOutput, Constants.Arm.INTAKE_DROP_SPEED)
        m_right_intake.set(ControlMode.PercentOutput, Constants.Arm.INTAKE_DROP_SPEED)
    }

    fun stopArm() {
        System.out.println("stop")
        m_arm.set(ControlMode.PercentOutput, 0.0)
    }
}
