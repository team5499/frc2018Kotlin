package frc.team5499.frc2018Kotlin.auto.actions

import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

import org.team5499.monkeyLib.auto.Action

class CrossedYBoundaryAction(yLine: Double, above: Boolean = true) : Action(0.0) {

    private val mLineCoord: Double
    private val mAbove: Boolean

    init {
        mLineCoord = yLine
        mAbove = above
    }

    override fun next(): Boolean {
        if (mAbove) {
            return (Drivetrain.pose.translation.y > mLineCoord)
        } else {
            return (Drivetrain.pose.translation.y < mLineCoord)
        }
    }
}
