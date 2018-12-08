package frc.team5499.frc2018Kotlin.auto.actions

import frc.team5499.frc2018Kotlin.utils.math.geometry.Vector2

import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

class WaitUntilRegionAction(lowerLeftCorner: Vector2, upperRightCorner: Vector2) : Action(0.0) {

    private val mLowerLeft: Vector2
    private val mUpperRight: Vector2

    init {
        mLowerLeft = lowerLeftCorner
        mUpperRight = upperRightCorner
    }

    override fun next(): Boolean {
        val driveTranslation = Drivetrain.pose.translation
        return (driveTranslation.x > mLowerLeft.x && driveTranslation.x < mUpperRight.x &&
            driveTranslation.y > mLowerLeft.y && driveTranslation.y < mUpperRight.y)
    }
}
