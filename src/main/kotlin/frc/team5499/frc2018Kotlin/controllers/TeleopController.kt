package frc.team5499.frc2018Kotlin.controllers

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.GenericHID.Hand

import frc.team5499.frc2018Kotlin.Constants
import frc.team5499.frc2018Kotlin.utils.DriveHelper
import frc.team5499.frc2018Kotlin.utils.DriveSignal
import frc.team5499.frc2018Kotlin.subsystems.Drivetrain
import frc.team5499.frc2018Kotlin.subsystems.Arm

object TeleopController : Controller() {

    enum class DriveConfig {
        CHEESY,
        SPACE,
        TANK
    }

    var driveConfig = Constants.Input.DRIVE_CONFIG

    val driver = XboxController(Constants.Input.DRIVER_PORT)
    val codriver = XboxController(Constants.Input.CODRIVER_PORT)

    override fun start() {
        driveConfig = Constants.Input.DRIVE_CONFIG
        Drivetrain.isBrakeMode = false
    }

    override fun update() {
        val signal: DriveSignal
        when (driveConfig) {
            DriveConfig.CHEESY -> {
                signal = DriveHelper.cheesyDrive(-driver.getY(Hand.kLeft),
                    driver.getX(Hand.kRight),
                    driver.getBumper(Hand.kRight),
                    false)
            }
            DriveConfig.SPACE -> {
                signal = DriveHelper.spaceDrive(-driver.getY(Hand.kLeft),
                    driver.getX(Hand.kRight),
                    driver.getBumper(Hand.kRight))
            }
            DriveConfig.TANK -> {
                signal = DriveHelper.tankDrive(-driver.getY(Hand.kLeft),
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
