package frc.team5499.frc2018Kotlin.controllers

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.GenericHID.Hand

import frc.team5499.frc2018Kotlin.Constants
import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

object TeleopController : Controller() {

    val driver = XboxController(Constants.Input.DRIVER_PORT)
    val codriver = XboxController(Constants.Input.CODRIVER_PORT)

    override fun start() {
        Drivetrain.isBrakeMode = false
    }

    override fun update() {
        val left = -driver.getY(Hand.kLeft)
        val right = -driver.getY(Hand.kRight)
        Drivetrain.setPercent(left, right)
    }

    override fun reset() {}
}
