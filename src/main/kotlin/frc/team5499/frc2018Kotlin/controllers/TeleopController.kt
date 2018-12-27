package frc.team5499.frc2018Kotlin.controllers

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.GenericHID.Hand

import frc.team5499.frc2018Kotlin.Constants
import frc.team5499.frc2018Kotlin.subsystems.Drivetrain
import frc.team5499.frc2018Kotlin.subsystems.Arm

import org.team5499.monkeyLib.Controller
import org.team5499.monkeyLib.input.DriveSignal
import org.team5499.monkeyLib.input.CheesyDriveHelper
import org.team5499.monkeyLib.input.SpaceDriveHelper
import org.team5499.monkeyLib.input.TankDriveHelper

object TeleopController : Controller() {

    enum class DriveConfig {
        CHEESY,
        SPACE,
        TANK
    }

    private var driveConfig = Constants.Input.DRIVE_CONFIG

    private val driver: XboxController
    private val codriver: XboxController

    private var cheesyDriveConfig: CheesyDriveHelper.CheesyDriveConfig

    private val cheesyHelper: CheesyDriveHelper
    private val tankHelper: TankDriveHelper
    private val spaceHelper: SpaceDriveHelper

    init {
        driver = XboxController(Constants.Input.DRIVER_PORT)
        codriver = XboxController(Constants.Input.CODRIVER_PORT)

        cheesyDriveConfig = CheesyDriveHelper.CheesyDriveConfig()
        cheesyDriveConfig.deadband = Constants.Input.THROTTLE_DEADBAND
        cheesyDriveConfig.quickstopDeadband = Constants.Input.QUICKSTOP_DEADBAND
        cheesyDriveConfig.quickstopWeight = Constants.Input.QUICKSTOP_WEIGHT
        cheesyDriveConfig.quickstopScalar = Constants.Input.QUICKSTOP_SCALAR
        cheesyDriveConfig.highWheelNonlinearity = Constants.Input.HIGH_WHEEL_NONLINEARITY
        cheesyDriveConfig.lowWheelNonlinearity = Constants.Input.LOW_WHEEL_NONLINEARITY
        cheesyDriveConfig.highNeginertiaScalar = Constants.Input.HIGH_NEGINERTIA_SCALAR
        cheesyDriveConfig.highSensitivity = Constants.Input.HIGH_SENSITIVITY
        cheesyDriveConfig.lowNeginertiaTurnScalar = Constants.Input.LOW_NEGINERTIA_TURN_SCALAR
        cheesyDriveConfig.lowNeginertiaThreshold = Constants.Input.LOW_NEGINERTIA_THRESHOLD
        cheesyDriveConfig.lowNeginertiaFarScalar = Constants.Input.LOW_NEGINERTIA_FAR_SCALAR
        cheesyDriveConfig.lowNeginertiaCloseScalar = Constants.Input.LOW_NEGINERTIA_CLOSE_SCALAR
        cheesyDriveConfig.lowSensitivity = Constants.Input.LOW_SENSITIVITY

        cheesyHelper = CheesyDriveHelper(cheesyDriveConfig)
        tankHelper = TankDriveHelper(Constants.Input.THROTTLE_DEADBAND, Constants.Input.SLOW_MULT)
        spaceHelper = SpaceDriveHelper(Constants.Input.THROTTLE_DEADBAND, Constants.Input.TURN_MULT)
    }

    override fun start() {
        driveConfig = Constants.Input.DRIVE_CONFIG
        Drivetrain.isBrakeMode = false
    }

    override fun update() {
        val signal: DriveSignal
        when (driveConfig) {
            DriveConfig.CHEESY -> {
                signal = cheesyHelper.calculateOutput(-driver.getY(Hand.kLeft),
                    driver.getX(Hand.kRight),
                    driver.getBumper(Hand.kRight),
                    false)
            }
            DriveConfig.SPACE -> {
                signal = spaceHelper.calculateOutput(-driver.getY(Hand.kLeft),
                    driver.getX(Hand.kRight),
                    driver.getBumper(Hand.kRight))
            }
            DriveConfig.TANK -> {
                signal = tankHelper.calculateOutput(-driver.getY(Hand.kLeft),
                    -driver.getY(Hand.kRight),
                    driver.getBumper(Hand.kRight))
            }
        }

        Drivetrain.setPercent(signal)

        Arm.setArm(codriver.getY(Hand.kRight))
        if (codriver.getBumper(Hand.kRight))
            Arm.intake()
        else Arm.setIntake(0.0)
    }

    fun rotateAuto(): Boolean {
        return codriver.getXButtonPressed()
    }

    override fun reset() {}
}
