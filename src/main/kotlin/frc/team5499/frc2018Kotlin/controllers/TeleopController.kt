package org.frc2018.fcr2018Kotlin.controllers

import frc.team5499.frc2018Kotlin.Constants
import frc.team5499.frc2018Kotlin.subsystems.Arm
import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class TeleopController {
    private val driver: XboxController = XboxController(Constants.DRIVER_PORT)
    private val codriver: XboxController = XboxController(Constants.CODRIVER_PORT)

    init {
        Drivetrain.getInstance().setBrakeMode(false)
        Drivetrain.getInstance().stop()
    }
    
    /*override*/ public fun handle() {
        var base: Double = driver.getY(Hand.kLeft) 
        if (Math.abs(driver.getX(Hand.kRight)) < Constants.XBOX_DEADBAND) {base = 0.0}

        var turn: Double = driver.getY(Hand.kLeft)
        if (Math.abs(driver.getY(Hand.kLeft)) < Constants.XBOX_DEADBAND) { turn = 0.0}
        if (!driver.getBumper(Hand.kRight)) {turn *= Constants.XBOX_TURN_MULTIPLIER}

        var arm: Double = codriver.getY(Hand.kLeft) * Constants.MAX_ARM_SPEED
        if(Math.abs(codriver.getY(Hand.kLeft)) < Constants.XBOX_DEADBAND){arm = 0.0}
        
        Drivetrain.getInstance().setPercent(-base + turn, -base - turn)
        Arm.getInstance().setArm(arm)

        if(codriver.getBumper(Hand.kRight)) {
            Arm.getInstance().intake()
        } else if(codriver.getTriggerAxis(Hand.kLeft) > Constants.XBOX_DEADBAND) {
             Arm.getInstance().spit()
        } else if(codriver.getTriggerAxis(Hand.kRight) > Constants.XBOX_DEADBAND){
            Arm.getInstance().hold()
        } else if(codriver.getBumper(Hand.kLeft)) {
            Arm.getInstance().drop()
        } else{
            Arm.getInstance().stopIntake();
        }
    }

    public fun getRotateAuto():Boolean{
        return codriver.getXButton()
    }

    public fun getRotateCube(): Boolean{
        return codriver.getAButton()
    }

    /*override*/ public fun reset() {}

    companion object{
        private val _instance: TeleopController = TeleopController()
        public fun getInstance(): TeleopController {
            return _instance
        } 
    }
}