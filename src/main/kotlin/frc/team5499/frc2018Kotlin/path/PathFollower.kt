package frc.team5499.frc2018Kotlin.path

import frc.team5499.frc2018Kotlin.Constants

import frc.team5499.frc2018Kotlin.utils.math.geometry.Vector2
import frc.team5499.frc2018Kotlin.utils.math.geometry.Pose2dWithCurvature

@SuppressWarnings("MagicNumber")
class PathFollower(path: Path) {

    private val mPath: Path
    private var mLastClosestPointIndex: Int
    // private var mLookahead: Vector2?

    init {
        mPath = path
        mLastClosestPointIndex = 0
        // mLookahead = null
    }

    fun update(currentRobotPose: Pose2dWithCurvature): PathFollowerOutput {
        var robotAngle = currentRobotPose.rotation.radians
        if (robotAngle == 0.0) robotAngle = Constants.EPSILON
        val lookahead = calculateLookahead(currentRobotPose)
        val curvature = calculateCurvature(currentRobotPose, lookahead, robotAngle)
        val velocityTarget = mPath.getVelocity(mLastClosestPointIndex)
        val leftVelo = velocityTarget * (2.0 + (curvature * Constants.TRACK_WIDTH)) / 2.0
        val rightVelo = velocityTarget * (2.0 - (curvature * Constants.TRACK_WIDTH)) / 2.0

        return PathFollowerOutput(leftVelo, rightVelo)
    }

    @Suppress("ComplexMethod")
    private fun calculateLookahead(robotPose: Pose2dWithCurvature): Vector2 {
        mLastClosestPointIndex = mPath.findClosestPointIndex(robotPose.pose, mLastClosestPointIndex)
        var lookahead: Vector2? = null
        for (i in mLastClosestPointIndex..mPath.pathLength - 2) {
            val begin = mPath.getPose(i)
            val end = mPath.getPose(i + 1)
            val d = end.translation - begin.translation
            val f = begin.translation - robotPose.translation

            val a = d.dot(d)
            val b = 2.0 * f.dot(d)
            val c = f.dot(f) - Math.pow(Constants.Path.LOOK_AHEAD_DISTANCE, 2.0)
            var dis = (b * b) - (4.0 * a * c)
            if (dis < 0.0) {
                continue
            } else {
                dis = Math.sqrt(dis)
                val t1 = (-b - dis) / (2.0 * a)
                val t2 = (-b + dis) / (2.0 * a)
                if (t1 >= 0 && t1 <= 1) {
                    val temp: Vector2 = d * t1
                    lookahead = begin.translation + temp
                    break
                } else if (t2 >= 0 && t2 <= 1) {
                    val temp = d * t2
                    lookahead = begin.translation + temp
                    break
                }
            }
            if (lookahead == null) {
                lookahead = mPath.endPose.translation
            } else {
                val distanceToEnd = robotPose.translation.distanceTo(mPath.endPose.translation)
                if (distanceToEnd < Constants.Path.LOOK_AHEAD_DISTANCE) {
                    lookahead = mPath.endPose.translation
                }
            }
        }

        return lookahead!!
    }

    private fun calculateCurvature(robotPose: Pose2dWithCurvature, lookahead: Vector2, robotAngle: Double): Double {
        val a = (1 / Math.tan(robotAngle))
        val b = -1
        val c = -(1 / Math.tan(robotAngle)) * robotPose.translation.y + robotPose.translation.x
        val x = Math.abs(a * lookahead.y + b * lookahead.x + c) / ((Math.sqrt(a * a + b * b)))
        val curvature = (2.0 * x) / (Math.pow(Constants.Path.LOOK_AHEAD_DISTANCE, 2.0))
        val side = Math.signum(
            Math.sin(robotAngle) * (lookahead.x - robotPose.translation.x) -
            Math.cos(robotAngle) * (lookahead.y - robotPose.translation.y)
        )
        return curvature * side
    }

    fun doneWithPath(robotPos: Pose2dWithCurvature): Boolean {
        val distance = robotPos.translation.distanceTo(mPath.endPose.translation)
        return distance < Constants.Path.LOOK_AHEAD_DISTANCE
    }

    fun reset() {
        mLastClosestPointIndex = 0
    }

    public data class PathFollowerOutput(val leftVelocity: Double = 0.0, val rightVelocity: Double = 0.0)
}
