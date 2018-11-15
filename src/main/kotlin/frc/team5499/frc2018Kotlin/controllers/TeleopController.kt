package frc.team5499.frc2018Kotlin.controllers

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.GenericHID.Hand

import frc.team5499.frc2018Kotlin.Constants
import frc.team5499.frc2018Kotlin.utils.CheesyDrive
import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

object TeleopController : Controller() {

    val driver = XboxController(Constants.Input.DRIVER_PORT)
    val codriver = XboxController(Constants.Input.CODRIVER_PORT)

    val cheesyDriver = CheesyDrive()

    override fun start() {
        Drivetrain.isBrakeMode = false
    }

    override fun update() {
        val signal = cheesyDriver.cheesyDrive(-driver.getY(Hand.kLeft),
            -driver.getX(Hand.kRight),
            driver.getBumper(Hand.kRight))
        Drivetrain.setPercent(signal)
    }

    override fun reset() {}
}
