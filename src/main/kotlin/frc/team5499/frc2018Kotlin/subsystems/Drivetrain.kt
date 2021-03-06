package frc.team5499.frc2018Kotlin.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.FollowerType
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.RemoteSensorSource
import com.ctre.phoenix.motorcontrol.SensorTerm
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced
import com.ctre.phoenix.sensors.PigeonIMU
import com.ctre.phoenix.motorcontrol.DemandType
import com.ctre.phoenix.ParamEnum

import frc.team5499.frc2018Kotlin.Constants

import org.team5499.monkeyLib.Subsystem
import org.team5499.monkeyLib.math.geometry.Vector2
import org.team5499.monkeyLib.math.geometry.Rotation2d
import org.team5499.monkeyLib.math.geometry.Pose2d
import org.team5499.monkeyLib.math.Position
import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.input.DriveSignal
import org.team5499.monkeyLib.util.Utils

@Suppress("LargeClass", "TooManyFunctions")
object Drivetrain : Subsystem() {

    // HARDWARE INIT
    private val mLeftMaster = LazyTalonSRX(Constants.Talons.LEFT_MASTER_PORT).apply {
        setInverted(false)
        setSensorPhase(false)
        setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, Constants.Talons.TALON_UPDATE_PERIOD_MS, 0)
    }

    private val mLeftSlave = LazyTalonSRX(Constants.Talons.LEFT_SLAVE_PORT).apply {
        setInverted(false)
        follow(mLeftMaster)
    }

    private val mRightMaster = LazyTalonSRX(Constants.Talons.RIGHT_MASTER_PORT).apply {
        setInverted(true)
        setSensorPhase(false)
        setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, Constants.Talons.TALON_UPDATE_PERIOD_MS, 0)
    }

    private val mRightSlave = LazyTalonSRX(Constants.Talons.RIGHT_SLAVE_PORT).apply {
        setInverted(true)
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

    private val position = Position()

    fun setPosition(newPos: Vector2) {
        position.positionVector = Vector2(newPos)
    }

    val pose: Pose2d
        get() = Pose2d(position.positionVector, heading)

    // hardware functions

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

    private var mGyroOffset: Rotation2d = Rotation2d.identity
        get() = field
        set(value) { field = value }

    var heading: Rotation2d
        get() {
            return Rotation2d.fromDegrees(mGyro.getFusedHeading()).rotateBy(mGyroOffset)
        }
        set(value) {
            println("SET HEADING: ${heading.degrees}")
            mGyroOffset = value.rotateBy(Rotation2d.fromDegrees(mGyro.getFusedHeading()).inverse())
            println("Gyro offset: ${mGyroOffset.degrees}")
        }

    val gyroAngularVelocity: Double
        get() {
            var xyz = doubleArrayOf(0.0, 0.0, 0.0)
            mGyro.getRawGyro(xyz)
            return xyz[1]
        }

    var leftDistance: Double
        get() {
            return Utils.encoderTicksToInches(
                Constants.ENCODER_TICKS_PER_ROTATION,
                Constants.WHEEL_CIR,
                mLeftMaster.sensorCollection.quadraturePosition
            )
        }
        set(inches) {
            mLeftMaster.sensorCollection.setQuadraturePosition(
                Utils.inchesToEncoderTicks(Constants.ENCODER_TICKS_PER_ROTATION,
                Constants.WHEEL_CIR,
                inches), 0)
        }

    var rightDistance: Double
        get() {
            return Utils.encoderTicksToInches(
                Constants.ENCODER_TICKS_PER_ROTATION,
                Constants.WHEEL_CIR,
                mRightMaster.sensorCollection.quadraturePosition
            )
        }
        set(inches) {
            mRightMaster.sensorCollection.setQuadraturePosition(
                Utils.inchesToEncoderTicks(Constants.ENCODER_TICKS_PER_ROTATION,
                Constants.WHEEL_CIR,
                inches), 0)
        }

    val leftVelocity: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            Constants.ENCODER_TICKS_PER_ROTATION,
            Constants.WHEEL_CIR,
            mLeftMaster.sensorCollection.quadratureVelocity
        )

    val rightVelocity: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            Constants.ENCODER_TICKS_PER_ROTATION,
            Constants.WHEEL_CIR,
            mRightMaster.sensorCollection.quadratureVelocity
        )

    val averageVelocity: Double
        get() = (leftVelocity + rightVelocity) / 2.0

    val leftVelocityError: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            Constants.ENCODER_TICKS_PER_ROTATION,
            Constants.WHEEL_CIR,
            mLeftMaster.getClosedLoopError(0)
        )

    val rightVelocityError: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            Constants.ENCODER_TICKS_PER_ROTATION,
            Constants.WHEEL_CIR,
            mRightMaster.getClosedLoopError(0)
        )

    val averageVelocityError: Double
        get() = (leftVelocityError + rightVelocityError) / 2.0

    val positionError: Double
        get() = Utils.encoderTicksToInches(
            Constants.ENCODER_TICKS_PER_ROTATION,
            Constants.WHEEL_CIR,
            mRightMaster.getClosedLoopError(0)
        )

    val anglePositionError: Double
        get() = Utils.talonAngleToDegrees(
            Constants.Gyro.TURN_UNITS_PER_ROTATION,
            mRightMaster.getClosedLoopError(1)
        )

    val turnError: Double
        get() = Utils.talonAngleToDegrees(
            Constants.Gyro.TURN_UNITS_PER_ROTATION,
            mRightMaster.getClosedLoopError(0)
        )

    val turnFixedError: Double
        get() = Utils.encoderTicksToInches(
            Constants.ENCODER_TICKS_PER_ROTATION,
            Constants.WHEEL_CIR,
            mRightMaster.getClosedLoopError(1)
        )

    // setup funcs
    private fun configForPercent() {
        isBrakeMode = false
        mLeftMaster.apply {
            // follow(null)
            configNominalOutputForward(0.0, 0)
            configNominalOutputReverse(0.0, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            setInverted(false)
            setSensorPhase(false)
        }

        mLeftSlave.apply {
            setInverted(false)
        }

        mRightMaster.apply {
            configNominalOutputForward(0.0, 0)
            configNominalOutputReverse(0.0, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            setInverted(true)
            setSensorPhase(false)
        }

        mRightSlave.apply {
            setInverted(true)
        }
    }

    private fun configForVelocity() {
        isBrakeMode = true
        mLeftMaster.apply {
            setInverted(false)
            // follow(null)
            configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            setSensorPhase(false)
            config_kP(0, Constants.PID.VEL_KP, 0)
            config_kI(0, Constants.PID.VEL_KI, 0)
            config_kD(0, Constants.PID.VEL_KD, 0)
            config_kF(0, Constants.PID.VEL_KF, 0)
            config_kP(1, 0.0, 0)
            config_kI(1, 0.0, 0)
            config_kD(1, 0.0, 0)
            config_kF(1, 0.0, 0)
            config_IntegralZone(0, 0, 0)
            configClosedLoopPeakOutput(0, 1.0, 0)
            config_IntegralZone(1, 0, 0)
            configClosedLoopPeakOutput(1, 0.0, 0)
            selectProfileSlot(0, 0)
            selectProfileSlot(1, 1)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.Talons.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 0, 0)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.Talons.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 1, 0)
        }

        mLeftSlave.apply {
            setInverted(false)
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
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            setSensorPhase(true)
            config_kP(0, Constants.PID.VEL_KP, 0)
            config_kI(0, Constants.PID.VEL_KI, 0)
            config_kD(0, Constants.PID.VEL_KD, 0)
            config_kF(0, Constants.PID.VEL_KF, 0)
            config_kP(1, 0.0, 0)
            config_kI(1, 0.0, 0)
            config_kD(1, 0.0, 0)
            config_kF(1, 0.0, 0)
            config_IntegralZone(0, 0, 0)
            configClosedLoopPeakOutput(0, 1.0, 0)
            config_IntegralZone(1, 0, 0)
            configClosedLoopPeakOutput(1, 0.0, 0)
            selectProfileSlot(0, 0)
            selectProfileSlot(1, 1)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.Talons.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 0, 0)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.Talons.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 1, 0)
        }

        mRightSlave.apply {}
    }

    private fun configForTurn() {
        isBrakeMode = true
        mLeftMaster.apply {
            configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, Constants.Talons.TALON_UPDATE_PERIOD_MS, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            follow(mRightMaster, FollowerType.AuxOutput1)
            setSensorPhase(false)
            setInverted(true)
        }

        mLeftSlave.apply {
            setInverted(true)
        }

        mRightMaster.apply {
            configRemoteFeedbackFilter(mLeftMaster.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor, 0, 0)
            configRemoteFeedbackFilter(mGyro.getDeviceID(), RemoteSensorSource.Pigeon_Yaw, 1, 0)
            configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, 0)
            configSensorTerm(SensorTerm.Sum1, FeedbackDevice.QuadEncoder, 0)
            configSelectedFeedbackSensor(FeedbackDevice.SensorSum, 1, 0)
            @Suppress("MagicNumber")
            configSelectedFeedbackCoefficient(0.5, 1, 0)
            configSelectedFeedbackCoefficient(
                (Constants.Gyro.TURN_UNITS_PER_ROTATION / Constants.Gyro.PIGEON_UNITS_PER_ROTATION).toDouble(), 0, 0)
            configSelectedFeedbackSensor(FeedbackDevice.RemoteSensor1, 0, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_12_Feedback1, Constants.Talons.TALON_UPDATE_PERIOD_MS, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, Constants.Talons.TALON_UPDATE_PERIOD_MS, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_14_Turn_PIDF1, Constants.Talons.TALON_UPDATE_PERIOD_MS, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            config_kP(0, Constants.PID.TURN_KP, 0)
            config_kI(0, Constants.PID.TURN_KI, 0)
            config_kD(0, Constants.PID.TURN_KD, 0)
            config_kF(0, Constants.PID.TURN_KF, 0)
            config_kP(1, Constants.PID.FIXED_KP, 0)
            config_kI(1, Constants.PID.FIXED_KI, 0)
            config_kD(1, Constants.PID.FIXED_KD, 0)
            config_kF(1, Constants.PID.FIXED_KF, 0)
            config_IntegralZone(0, Constants.PID.TURN_IZONE, 0)
            configClosedLoopPeakOutput(0, Constants.PID.TURN_MAX_OUTPUT, 0)
            config_IntegralZone(1, Constants.PID.FIXED_IZONE, 0)
            configClosedLoopPeakOutput(1, Constants.PID.FIXED_MAX_OUTPUT, 0)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.Talons.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 0, 0)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.Talons.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 1, 0)
            configAuxPIDPolarity(!Constants.PID.INVERT_TURN_AUX_PIDF, 0)
            selectProfileSlot(0, 0)
            selectProfileSlot(1, 1)
            setSensorPhase(true)
        }
    }

    private fun configForPosition() {
        isBrakeMode = true
        mLeftMaster.apply {
            configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, Constants.Talons.TALON_UPDATE_PERIOD_MS, 0)
            follow(mRightMaster, FollowerType.AuxOutput1)
            setSensorPhase(false)
            setInverted(false)
        }

        mLeftSlave.apply {
            setInverted(false)
        }

        mRightMaster.apply {
            configRemoteFeedbackFilter(mLeftMaster.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor, 0, 0)
            configRemoteFeedbackFilter(mGyro.getDeviceID(), RemoteSensorSource.Pigeon_Yaw, 1, 0)
            configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, 0)
            configSensorTerm(SensorTerm.Sum1, FeedbackDevice.QuadEncoder, 0)
            configSelectedFeedbackSensor(FeedbackDevice.SensorSum, 0, 0)
            @Suppress("MagicNumber")
            configSelectedFeedbackCoefficient(0.5, 0, 0)
            configSelectedFeedbackCoefficient(
                (Constants.Gyro.TURN_UNITS_PER_ROTATION / Constants.Gyro.PIGEON_UNITS_PER_ROTATION).toDouble(), 1, 0)
            configSelectedFeedbackSensor(FeedbackDevice.RemoteSensor1, 1, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_12_Feedback1, Constants.Talons.TALON_UPDATE_PERIOD_MS, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, Constants.Talons.TALON_UPDATE_PERIOD_MS, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_14_Turn_PIDF1, Constants.Talons.TALON_UPDATE_PERIOD_MS, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, Constants.Talons.TALON_UPDATE_PERIOD_MS, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            config_kP(0, Constants.PID.POS_KP, 0)
            config_kI(0, Constants.PID.POS_KI, 0)
            config_kD(0, Constants.PID.POS_KD, 0)
            config_kF(0, Constants.PID.POS_KF, 0)
            config_kP(1, Constants.PID.ANGLE_KP, 0)
            config_kI(1, Constants.PID.ANGLE_KI, 0)
            config_kD(1, Constants.PID.ANGLE_KF, 0)
            config_kF(1, Constants.PID.ANGLE_KF, 0)
            config_IntegralZone(0, Constants.PID.POS_IZONE, 0)
            configClosedLoopPeakOutput(0, Constants.PID.POS_MAX_OUTPUT, 0)
            config_IntegralZone(1, Constants.PID.ANGLE_IZONE, 0)
            configClosedLoopPeakOutput(1, Constants.PID.ANGLE_MAX_OUTPUT, 0)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.Talons.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 0, 0)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.Talons.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 1, 0)
            selectProfileSlot(0, 0)
            selectProfileSlot(1, 1)
            configAuxPIDPolarity(!Constants.PID.INVERT_ANGLE_AUX_PIDF, 0)
            setSensorPhase(true)
        }
    }

    // drive funcs
    fun setPercent(left: Double, right: Double) {
        setPercent(left, right, false)
    }

    fun setPercent(left: Double, right: Double, brakeMode: Boolean) {
        driveMode = DriveMode.OPEN_LOOP
        mLeftMaster.set(ControlMode.PercentOutput, left)
        mRightMaster.set(ControlMode.PercentOutput, right)
        isBrakeMode = brakeMode
    }

    fun setPercent(signal: DriveSignal) {
        setPercent(signal.left, signal.right, signal.brakeMode)
    }

    fun setPosition(distance: Double) {
        driveMode = DriveMode.POSITION
        val absDistance = Utils.inchesToEncoderTicks(
            Constants.ENCODER_TICKS_PER_ROTATION,
            Constants.WHEEL_CIR,
            ((leftDistance + rightDistance) / 2.0) + distance
        )
        val angleTarget = mRightMaster.getSelectedSensorPosition(1)
        mRightMaster.set(ControlMode.Position, absDistance.toDouble(), DemandType.AuxPID, angleTarget.toDouble())
    }

    fun setTurn(angle: Double) {
        driveMode = DriveMode.TURN
        val fixedDistance = Utils.inchesToEncoderTicks(
            Constants.ENCODER_TICKS_PER_ROTATION,
            Constants.WHEEL_CIR,
            (leftDistance + rightDistance) / 2.0
        )
        val angleTarget = mRightMaster.getSelectedSensorPosition(1) +
            Utils.degreesToTalonAngle(Constants.Gyro.TURN_UNITS_PER_ROTATION, angle)
        mRightMaster.set(ControlMode.Position, angleTarget.toDouble(), DemandType.AuxPID, fixedDistance.toDouble())
    }

    fun setVelocity(leftSpeed: Double, rightSpeed: Double) {
        driveMode = DriveMode.VELOCITY
        val left = Math.min(Constants.MAX_VELOCITY_SETPOINT, leftSpeed)
        val right = Math.min(Constants.MAX_VELOCITY_SETPOINT, rightSpeed)
        mLeftMaster.set(ControlMode.Velocity,
            Utils.inchesPerSecondToEncoderTicksPer100Ms(
                Constants.ENCODER_TICKS_PER_ROTATION,
                Constants.WHEEL_CIR,
                left)
            )
        mRightMaster.set(ControlMode.Velocity,
            Utils.inchesPerSecondToEncoderTicksPer100Ms(
                Constants.ENCODER_TICKS_PER_ROTATION,
                Constants.WHEEL_CIR,
                right)
            )
    }

    // misc functions

    // super class methods
    override fun update() {
        position.update(leftDistance, rightDistance, heading.degrees)
    }

    override fun reset() {
        // zeroGyro()
        leftDistance = 0.0
        rightDistance = 0.0
        position.reset()
        isBrakeMode = false
    }

    override fun stop() {
        setPercent(0.0, 0.0)
        mLeftMaster.neutralOutput()
        mRightMaster.neutralOutput()
        isBrakeMode = false
    }
}
