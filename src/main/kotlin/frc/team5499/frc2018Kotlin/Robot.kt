package frc.team5499.frc2018Kotlin

import edu.wpi.first.wpilibj.TimedRobot

import frc.team5499.frc2018Kotlin.controllers.TeleopController
import frc.team5499.frc2018Kotlin.controllers.AutoController
import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

class Robot : TimedRobot() {

    override fun robotInit() {
        super.setPeriod(Constants.UPDATE_PERIOD)
    }

    override fun robotPeriodic() {}

    override fun disabledInit() {
        Drivetrain.reset()
        AutoController.reset()
        TeleopController.reset()
        Drivetrain.isBrakeMode = false
    }

    override fun disabledPeriodic() {
        if (TeleopController.rotateAuto()) {
            AutoController.rotateAuto()
        }
    }

    override fun autonomousInit() {
        Drivetrain.reset()
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
