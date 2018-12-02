package frc.team5499.frc2018Kotlin.controllers

import frc.team5499.frc2018Kotlin.path.PathFollower

import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

object AutoController : Controller() {

    private var follower: PathFollower?

    init {
        follower = null
    }

    override fun start() {
        // PathGenerator.generatePathSet()
        // follower = PathFollower(PathGenerator.pathSet!!.startLeftSwitchPath)
        // val output = follower!!.update(Drivetrain.pose)
        // Drivetrain.setVelocity(output.leftVelocity, output.rightVelocity)
        @Suppress("MagicNumber")
        Drivetrain.setVelocity(10.0, 10.0)
    }

    override fun update() {
        println("Velocity Error: ${Drivetrain.averageVelocityError}")
        // val output = follower!!.update(Drivetrain.pose)
        // Drivetrain.setPercent(output.leftVelocity, output.rightVelocity)
    }

    override fun reset() {}
}
