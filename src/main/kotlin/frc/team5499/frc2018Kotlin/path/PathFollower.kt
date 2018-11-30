package frc.team5499.frc2018Kotlin.path

import frc.team5499.frc2018Kotlin.Constants

import frc.team5499.frc2018Kotlin.utils.math.geometry.Vector2
import frc.team5499.frc2018Kotlin.utils.math.geometry.Pose2dWithCurvature

class PathFollower(path: Path) {

    private val mPath: Path
    private var mLastClosestPointIndex: Int
    private var mLookahead: Vector2?

    init {
        mPath = path
        mLastClosestPointIndex = 0
        mLookahead = null
    }

    fun update(currentRobotPose: Pose2dWithCurvature): PathFollowerOutput {
        var robotAngle = currentRobotPose.rotation.radians
        if (robotAngle == 0.0) robotAngle = Constants.EPSILON
        // mLookahead = calculateLookahead(currentRobotPose)
        return PathFollowerOutput(0.0, 0.0)
    }

    // private fun calculateLookahead(robotPose: Pose2dWithCurvature): Vector2? {

    //     return null
    // }

    // private fun calculateVelocityTarget(robotPose: Pose2dWithCurvature) {

    // }

    // private fun calculateCurvature(robotPose: Pose2dWithCurvature) {

    // }

    fun doneWithPath(): Boolean {
        return false
    }

    public data class PathFollowerOutput(val leftVelocity: Double = 0.0, val rightVelocity: Double = 0.0)
}
