package frc.team5499.frc2018Kotlin

import edu.wpi.first.wpilibj.TimedRobot

import frc.team5499.frc2018Kotlin.controllers.TeleopController
import frc.team5499.frc2018Kotlin.controllers.AutoController
import frc.team5499.frc2018Kotlin.subsystems.Drivetrain
import frc.team5499.frc2018Kotlin.subsystems.Arm
import frc.team5499.frc2018Kotlin.subsystems.Intake

import frc.team5499.frc2018Kotlin.utils.CustomLogger

class Robot : TimedRobot() {

    override fun robotInit() {
        super.setPeriod(Constants.UPDATE_PERIOD)
        CustomLogger.LOG.info("test")
        // val mediaDir: File = File("~/media/")
        // println(mediaDir.listFiles())
    }

    override fun robotPeriodic() {}

    override fun disabledInit() {}

    override fun disabledPeriodic() {}

    override fun autonomousInit() {
        Drivetrain.reset()
        Arm.reset()
        Intake.reset()
        AutoController.reset()
        AutoController.start()
    }

    override fun autonomousPeriodic() {
        Drivetrain.update()
        Arm.update()
        Intake.update()
        AutoController.update()
    }

    override fun teleopInit() {
        TeleopController.reset()
        TeleopController.start()
    }

    override fun teleopPeriodic() {
        Drivetrain.update()
        Arm.update()
        Intake.update()
        TeleopController.update()
    }

    override fun testInit() {}

    override fun testPeriodic() {}
}
