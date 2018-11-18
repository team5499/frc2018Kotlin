package frc.team5499.frc2018Kotlin.controllers

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.GenericHID.Hand

import frc.team5499.frc2018Kotlin.Constants
import frc.team5499.frc2018Kotlin.utils.DriveHelper
import frc.team5499.frc2018Kotlin.utils.DriveSignal
import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

object TeleopController : Controller() {

    enum class DriveConfig {
        CHEESY,
        SPACE,
        TANK
    }

    var driveConfig = Constants.Input.DRIVE_CONFIG

    val driver = XboxController(Constants.Input.DRIVER_PORT)
    val codriver = XboxController(Constants.Input.CODRIVER_PORT)

    val driverHelper = DriveHelper()

    override fun start() {
        driveConfig = Constants.Input.DRIVE_CONFIG
        Drivetrain.isBrakeMode = false
    }

    override fun update() {
        val signal: DriveSignal
        when (driveConfig) {
            DriveConfig.CHEESY -> {
                signal = driverHelper.cheesyDrive(-driver.getY(Hand.kLeft),
                    driver.getX(Hand.kRight),
                    driver.getBumper(Hand.kRight),
                    false)
            }
            DriveConfig.SPACE -> {
                signal = driverHelper.spaceDrive(-driver.getY(Hand.kLeft),
                    driver.getX(Hand.kRight),
                    driver.getBumper(Hand.kRight))
            }
            else -> {
                signal = DriveSignal(-driver.getY(Hand.kLeft), -driver.getY(Hand.kRight))
            }
        }

        Drivetrain.setPercent(signal)
    }

    override fun reset() {}
}
