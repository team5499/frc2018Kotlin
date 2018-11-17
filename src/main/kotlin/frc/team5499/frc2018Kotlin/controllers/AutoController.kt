package frc.team5499.frc2018Kotlin.controllers

import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

object AutoController : Controller() {

    override fun start() {
        @Suppress("MagicNumber")
        Drivetrain.setPosition(60.0)
    }

    override fun update() {}

    override fun reset() {}
}
