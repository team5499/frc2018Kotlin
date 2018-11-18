package frc.team5499.frc2018Kotlin.controllers

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.GenericHID.Hand

import frc.team5499.frc2018Kotlin.Constants
import frc.team5499.frc2018Kotlin.subsystems.Drivetrain
import frc.team5499.frc2018Kotlin.subsystems.Arm

object TeleopController : Controller() {

    val driver = XboxController(Constants.Input.DRIVER_PORT)
    val codriver = XboxController(Constants.Input.CODRIVER_PORT)

    @Suppress("ComplexMethod")
    public fun handle() {
        var base: Double = driver.getY(Hand.kLeft)
        if (Constants.Controller.XBOX_DEADBAND > Math.abs(driver.getY(Hand.kLeft))) {
            base = 0.0
        }
        var turn: Double = driver.getX(Hand.kRight)
        if (Constants.Controller.XBOX_DEADBAND > Math.abs(driver.getX(Hand.kRight))) {
            turn = 0.0
        }
        if (driver.getBumper(Hand.kRight)) {
            turn *= Constants.Controller.XBOX_TURN_MULTIPLIER
        }

        var arm: Double = codriver.getY(Hand.kLeft) * Constants.Arm.MAX_ARM_SPEED
        if (Constants.Controller.XBOX_DEADBAND > codriver.getY(Hand.kLeft)) {
            arm = 0.0
        }

        Drivetrain.setPercent(-base + turn, -base + turn)
        Arm.setArm(arm)

        if (codriver.getBumper(Hand.kRight)) {
            Arm.intake()
        } else if (codriver.getTriggerAxis(Hand.kLeft) > Constants.Controller.XBOX_DEADBAND) {
            Arm.spit()
        } else if (codriver.getTriggerAxis(Hand.kRight) > Constants.Controller.XBOX_DEADBAND) {
            Arm.hold()
        } else if (codriver.getBumper(Hand.kLeft)) {
            Arm.drop()
        } else {
            Arm.stopIntake()
        }
    }

    public override fun start() {
        Drivetrain.isBrakeMode = false
        Drivetrain.stop()
    }

    public override fun update() {
        val left = -driver.getY(Hand.kLeft)
        val right = -driver.getY(Hand.kRight)
        Drivetrain.setPercent(left, right)
    }

    public override fun reset() {}
}
