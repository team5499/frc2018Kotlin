package frc.team5499.frc2018Kotlin.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX

import frc.team5499.frc2018Kotlin.Constants

object Arm : Subsystem() {
    var m_arm: TalonSRX = TalonSRX(Constants.Arm.ARM_PORT).apply {
        setInverted(Constants.Arm.INVERT_ARM)
    }
    var m_left_intake: TalonSRX = TalonSRX(Constants.Arm.LEFT_INTAKE_PORT).apply {
    }
    var m_right_intake: TalonSRX

    init {
        m_arm = TalonSRX(Constants.Arm.ARM_PORT)
        m_left_intake = TalonSRX(Constants.Arm.LEFT_INTAKE_PORT)
        m_right_intake = TalonSRX(Constants.Arm.RIGHT_INTAKE_PORT)

        m_left_intake.setInverted(Constants.Arm.INVERT_INTAKE)
        m_right_intake.setInverted(Constants.Arm.INVERT_INTAKE)
    }

    override fun update() {
    }

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

    fun stopArm() {
        System.out.println("stop")
        m_arm.set(ControlMode.PercentOutput, 0.0)
    }
}
