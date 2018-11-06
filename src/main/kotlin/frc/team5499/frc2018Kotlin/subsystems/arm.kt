package frc.team5499.frc2018Kotlin.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX

import frc.team5499.frc2018Kotlin.Constants

public class Arm() {
    var m_arm: TalonSRX
    var m_left_intake: TalonSRX
    var m_right_intake: TalonSRX

    init{
        m_arm = TalonSRX(Constants.Talons.ARM_PORT)
        m_left_intake = TalonSRX(Constants.Talons.LEFT_INTAKE_PORT)
        m_right_intake = TalonSRX(Constants.Talons.RIGHT_INTAKE_PORT)

        m_left_intake.setInverted(Constants.Talons.INVERT_INTAKE)
        m_right_intake.setInverted(Constants.Talons.INVERT_INTAKE)

        m_arm.setInverted(Constants.Talons.INVERT_ARM)
    }

    /*override*/ fun update() {

    }

    fun intake() {
        m_left_intake.setNeutralMode(NeutralMode.Brake)
        m_right_intake.setNeutralMode(NeutralMode.Brake)
        m_left_intake.set(ControlMode.PercentOutput, Constants.Talons.INTAKE_SPEED)
        m_right_intake.set(ControlMode.PercentOutput, Constants.Talons.INTAKE_SPEED)
    }

    public fun hold() {
        m_left_intake.setNeutralMode(NeutralMode.Brake)
        m_right_intake.setNeutralMode(NeutralMode.Brake)
        m_left_intake.set(ControlMode.PercentOutput, Constants.Talons.INTAKE_HOLD_SPEED)
        m_right_intake.set(ControlMode.PercentOutput, Constants.Talons.INTAKE_HOLD_SPEED)
    }

    public fun drop() {
        m_left_intake.setNeutralMode(NeutralMode.Brake)
        m_right_intake.setNeutralMode(NeutralMode.Brake)
        m_left_intake.set(ControlMode.PercentOutput, Constants.Talons.INTAKE_HOLD_SPEED)
        m_right_intake.set(ControlMode.PercentOutput, Constants.Talons.INTAKE_HOLD_SPEED)
    }

    public fun spit() {
        m_left_intake.setNeutralMode(NeutralMode.Brake)
        m_right_intake.setNeutralMode(NeutralMode.Brake)
        m_left_intake.set(ControlMode.PercentOutput, Constants.Talons.INTAKE_SPIT_SPEED)
        m_right_intake.set(ControlMode.PercentOutput, Constants.Talons.INTAKE_SPIT_SPEED)
    }

    public fun stopIntake() {
        m_left_intake.set(ControlMode.PercentOutput, 0.0)
        m_right_intake.set(ControlMode.PercentOutput, 0.0)
    }

    public fun setArm(speed:Double) {
        var speed:Double = speed
        if (Math.abs(speed) > Constants.Talons.MAX_ARM_SPEED) {
            speed = Math.signum(speed) * Constants.Talons.MAX_ARM_SPEED
        }
        m_arm.set(ControlMode.PercentOutput, speed)
    }

    /*override*/ public fun stop() {
        setArm(0.0)
        stopIntake()
    }

    /*override*/ public fun reset() {
        m_left_intake.setNeutralMode(NeutralMode.Coast)
        m_right_intake.setNeutralMode(NeutralMode.Coast)
    }

    
    companion object{
        private val _instance : Arm = Arm()
        public fun getInstance(): Arm {
            return _instance
        }
    }
    
}