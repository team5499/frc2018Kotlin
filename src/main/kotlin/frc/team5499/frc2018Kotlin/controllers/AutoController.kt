package frc.team5499.frc2018Kotlin.controllers

import frc.team5499.frc2018Kotlin.path.PathGenerator

object AutoController : Controller() {

    override fun start() {
        PathGenerator.generatePathSet()
    }

    override fun update() {
    }

    public override fun reset() {}
}
