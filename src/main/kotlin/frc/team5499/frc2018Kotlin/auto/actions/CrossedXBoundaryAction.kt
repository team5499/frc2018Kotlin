package frc.team5499.frc2018Kotlin.auto.actions

import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

class CrossedXBoundaryAction(xLine: Double, lessThan: Boolean = false) : Action(0.0) {

    private val mLineCoord: Double
    private val mLessThan: Boolean

    init {
        mLineCoord = xLine
        mLessThan = lessThan
    }

    override fun next(): Boolean {
        if (mLessThan) {
            return (Drivetrain.pose.translation.x < mLineCoord)
        } else {
            return (Drivetrain.pose.translation.x > mLineCoord)
        }
    }
}
