package frc.team5499.frc2018Kotlin.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.sensors.PigeonIMU
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced
import com.ctre.phoenix.motorcontrol.NeutralMode

import frc.team5499.frc2018Kotlin.Constants

object Drivetrain : Subsystem() {

    // HARDWARE INIT
    val mLeftMaster = TalonSRX(Constants.Talons.LEFT_MASTER_PORT).apply {
        inverted = false
        setSensorPhase(false)
        setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, Constants.Talons.TALON_UPDATE_PERIOD_MS, 0)
    }

    private val mLeftSlave = TalonSRX(Constants.Talons.LEFT_SLAVE_PORT).apply {
        inverted = false
        follow(mLeftMaster)
    }

    val mRightMaster = TalonSRX(Constants.Talons.RIGHT_MASTER_PORT).apply {
        inverted = true
        setSensorPhase(false)
        setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, Constants.Talons.TALON_UPDATE_PERIOD_MS, 0)
    }

    private val mRightSlave = TalonSRX(Constants.Talons.RIGHT_SLAVE_PORT).apply {
        inverted = true
        follow(mRightMaster)
    }

    private val mGyro = PigeonIMU(Constants.Gyro.GYRO_PORT_NUMBER)

    // class member init

    private enum class DriveMode {
        OPEN_LOOP,
        POSITION,
        TURN,
        VELOCITY
    }

    private var driveMode: DriveMode = DriveMode.OPEN_LOOP

    var isBrakeMode = false
        set(value: Boolean) {
            if (value == field) return
            val mode = if (value) NeutralMode.Brake else NeutralMode.Coast
            mLeftMaster.setNeutralMode(mode)
            mLeftSlave.setNeutralMode(mode)
            mRightMaster.setNeutralMode(mode)
            mRightSlave.setNeutralMode(mode)
            field = value
        }
        get() = field

    // hardware functions

    fun zeroGyro() = mGyro.setYaw(0.0, 0)

    fun getGyroAngle(): Double {
        var ypr = doubleArrayOf(0.0, 0.0, 0.0)
        mGyro.getYawPitchRoll(ypr)
        return ypr[0]
    }

    fun getGyroHeading(): Double {
        var a = getGyroAngle()
        while (a >= 360) a -= 360
        while (a < 0) a += 360
        return a
    }

    fun setGyroAngle(value: Double) {
        mGyro.setYaw(value, 0)
    }

    fun getLeftDistance(): Double {
        // mLeftMaster.get
        return 0.0
    }

    // driving funcs

    private fun configForPercent() {}

    fun setPercent(left: Double, right: Double) {
        if (driveMode == DriveMode.OPEN_LOOP) {
            configForPercent()
            driveMode = DriveMode.OPEN_LOOP
        }
        mLeftMaster.set(ControlMode.PercentOutput, left)
        mRightMaster.set(ControlMode.PercentOutput, right)
    }

    // subclass methods

    override fun update() {}

    override fun reset() {
        zeroGyro()
    }

    override fun stop() {
        setPercent(0.0, 0.0)
        mLeftMaster.neutralOutput()
        mRightMaster.neutralOutput()
        isBrakeMode = false
    }

    override fun broadcastToDashboard() {}
}
