package frc.team5499.frc2018Kotlin.controllers

import frc.team5499.frc2018Kotlin.path.PathFollower
import frc.team5499.frc2018Kotlin.path.PathGenerator

import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

object AutoController : Controller() {

    private var follower: PathFollower?

    init {
        follower = null
    }

    override fun start() {
        Drivetrain.isBrakeMode = true
        PathGenerator.generatePathSet()
        follower = PathFollower(PathGenerator.pathSet!!.startRightSwitchPath)
        val output = follower!!.update(Drivetrain.pose)
        Drivetrain.setVelocity(output.leftVelocity, output.rightVelocity)
    }

    override fun update() {
        // println("Velocity Error: ${Drivetrain.averageVelocityError}")
        val output = follower!!.update(Drivetrain.pose)
        Drivetrain.setVelocity(output.leftVelocity, output.rightVelocity)
    }

    public override fun reset() {}

    // private _instance: AutoController = AutoController()
}
