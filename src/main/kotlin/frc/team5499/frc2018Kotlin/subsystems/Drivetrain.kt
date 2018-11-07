package frc.team5499.frc2018Kotlin.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.RemoteSensorSource
import com.ctre.phoenix.motorcontrol.SensorTerm
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.sensors.PigeonIMU

import frc.team5499.frc2018Kotlin.Constants
import frc.team5499.frc2018Kotlin.Position
import frc.team5499.frc2018Kotlin.utils.Vector2
import frc.team5499.frc2018Kotlin.utils.Utils

@Suppress("TooManyFunctions")
object Drivetrain : Subsystem() {

    // HARDWARE INIT
    private val mLeftMaster = TalonSRX(Constants.Talons.LEFT_MASTER_PORT).apply {
        inverted = false
        setSensorPhase(false)
        setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, Constants.Talons.TALON_UPDATE_PERIOD_MS, 0)
    }

    private val mLeftSlave = TalonSRX(Constants.Talons.LEFT_SLAVE_PORT).apply {
        inverted = false
        follow(mLeftMaster)
    }

    private val mRightMaster = TalonSRX(Constants.Talons.RIGHT_MASTER_PORT).apply {
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
        set(value) {
            if (value == field) return
            when (value) {
                DriveMode.OPEN_LOOP -> {
                    configForPercent()
                }
                DriveMode.TURN -> {
                    configForTurn()
                }
                DriveMode.POSITION -> {
                    configForPosition()
                }
                DriveMode.VELOCITY -> {
                    configForVelocity()
                }
            }
            field = value
        }
        get() = field

    var isBrakeMode = false
        set(value) {
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

    var gyroAngle: Double
        get() {
            var ypr = doubleArrayOf(0.0, 0.0, 0.0)
            mGyro.getYawPitchRoll(ypr)
            return ypr[0]
        }
        set(value) {
            mGyro.setYaw(value, 0)
        }

    val gyroAngularVelocity: Double
        get() {
            var xyz = doubleArrayOf(0.0, 0.0, 0.0)
            mGyro.getRawGyro(xyz)
            return xyz[1]
        }

    fun zeroGyro() {
        gyroAngle = 0.0
    }

    var leftDistance: Double
        get() {
            return Utils.encoderTicksToInches(mLeftMaster.sensorCollection.quadraturePosition)
        }
        set(inches) {
            mLeftMaster.sensorCollection.setQuadraturePosition(Utils.inchesToEncoderTicks(inches), 0)
        }

    var rightDistance: Double
        get() {
            return Utils.encoderTicksToInches(mRightMaster.sensorCollection.quadraturePosition)
        }
        set(inches) {
            mRightMaster.sensorCollection.setQuadraturePosition(Utils.inchesToEncoderTicks(inches), 0)
        }

    val leftVelocity: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(mLeftMaster.sensorCollection.quadratureVelocity)

    val rightVelocity: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(mRightMaster.sensorCollection.quadratureVelocity)

    // setup funcs
    private fun configForPercent() {
        mLeftMaster.apply {
            configNominalOutputForward(0.0, 0)
            configNominalOutputReverse(0.0, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            inverted = false
            setSensorPhase(false)
        }

        mLeftSlave.apply {
            mLeftSlave.inverted = false
        }

        mRightMaster.apply {
            configNominalOutputForward(0.0, 0)
            configNominalOutputReverse(0.0, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            inverted = true
            setSensorPhase(false)
        }

        mRightSlave.apply {
            inverted = true
        }
    }

    private fun configForVelocity() {
        mLeftMaster.apply {
            configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0)
            inverted = false
        }

        mLeftSlave.apply {
            inverted = false
        }

        mRightMaster.apply {
            configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0)
            configRemoteFeedbackFilter(0x00, RemoteSensorSource.Off, 0, 0)
            configRemoteFeedbackFilter(0x00, RemoteSensorSource.Off, 1, 0)
            configSensorTerm(SensorTerm.Sum0, FeedbackDevice.None, 0)
            configSensorTerm(SensorTerm.Sum1, FeedbackDevice.None, 0)
            configSelectedFeedbackCoefficient(1.0, 1, 0)
            configSelectedFeedbackCoefficient(1.0, 0, 0)
            configSelectedFeedbackSensor(FeedbackDevice.None, 1, 0)
        }

        mRightSlave.apply {}
    }

    private fun configForTurn() {}

    private fun configForPosition() {}

    // drive funcs
    fun setPercent(left: Double, right: Double) {
        driveMode = DriveMode.OPEN_LOOP
        mLeftMaster.set(ControlMode.PercentOutput, left)
        mRightMaster.set(ControlMode.PercentOutput, right)
    }

    fun setPosition(leftDistance: Double, rightDistance: Double) {
        driveMode = DriveMode.POSITION
        mLeftMaster.set(ControlMode.Position, Utils.inchesToEncoderTicks(leftDistance).toDouble())
        mRightMaster.set(ControlMode.Position, Utils.inchesToEncoderTicks(rightDistance).toDouble())
    }

    fun setTurn(angle: Double) {
        driveMode = DriveMode.TURN
    }

    fun setVelocity(leftSpeed: Double, rightSpeed: Double) {
        driveMode = DriveMode.VELOCITY
    }

    // misc functions
    val position: Vector2
        get() = Position.positionVector

    // super class methods
    override fun update() {
        Position.update(leftDistance, rightDistance, gyroAngle)
    }

    override fun reset() {
        zeroGyro()
        leftDistance = 0.0
        rightDistance = 0.0
        Position.reset()
        isBrakeMode = false
    }

    override fun stop() {
        setPercent(0.0, 0.0)
        mLeftMaster.neutralOutput()
        mRightMaster.neutralOutput()
        isBrakeMode = false
    }

    override fun broadcastToDashboard() {}
}
