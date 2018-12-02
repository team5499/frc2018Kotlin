package frc.team5499.frc2018Kotlin

import edu.wpi.first.wpilibj.TimedRobot

import frc.team5499.frc2018Kotlin.controllers.TeleopController
import frc.team5499.frc2018Kotlin.controllers.AutoController
import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

import frc.team5499.frc2018Kotlin.utils.math.geometry.Rotation2d

class Robot : TimedRobot() {

    private var canRotateAuto = true

    override fun robotInit() {
        super.setPeriod(Constants.UPDATE_PERIOD)
    }

    override fun robotPeriodic() {}

    override fun disabledInit() {
        Drivetrain.isBrakeMode = false
    }

    override fun disabledPeriodic() {
        if (TeleopController.rotateAuto() && canRotateAuto) {
            AutoController.rotateAuto()
            canRotateAuto = false
        } else if (!TeleopController.rotateAuto()) {
            canRotateAuto = true
        }
    }

    override fun autonomousInit() {
        Drivetrain.reset()
        @Suppress("MagicNumber")
        Drivetrain.heading = Rotation2d.fromDegrees(180.0) // starting backwards
        AutoController.reset()
        AutoController.start()
    }

    override fun autonomousPeriodic() {
        Drivetrain.update()
        AutoController.update()
    }

    override fun teleopInit() {
        TeleopController.reset()
        TeleopController.start()
    }

    override fun teleopPeriodic() {
        Drivetrain.update()
        TeleopController.update()
    }

    override fun testInit() {}

    override fun testPeriodic() {}
}
