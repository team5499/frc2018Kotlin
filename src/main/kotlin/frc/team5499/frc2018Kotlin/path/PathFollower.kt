package frc.team5499.frc2018Kotlin.path

import frc.team5499.frc2018Kotlin.Constants

import frc.team5499.frc2018Kotlin.utils.math.geometry.Vector2
import frc.team5499.frc2018Kotlin.utils.math.geometry.Pose2d

/**
* class that contains methods to follow a list of coordinates
* used during autonomous
* @property path the path the follower will follow
*/
@SuppressWarnings("MagicNumber")
class PathFollower(path: Path) {

    private val mPath: Path
    private var mLastClosestPointIndex: Int

    init {
        mPath = path
        mLastClosestPointIndex = 0
    }

    /**
    * Function used by path follower to be run in every periodic tick to calculate
    * velocities based on current path, robot location, robot angle, and target velocities
    * @param currentRobotPose the current position and rotation of the drivetrain
    * @return velocities for the left and right sides of the drivetrain
    */
    fun update(currentRobotPose: Pose2d): PathFollowerOutput {
        var robotAngle = currentRobotPose.rotation.radians
        if (robotAngle == 0.0) robotAngle = Constants.EPSILON
        val lookahead = calculateLookahead(currentRobotPose)
        val curvature = calculateCurvature(currentRobotPose, lookahead, robotAngle)
        val velocityTarget = mPath.getVelocity(mLastClosestPointIndex)
        val negate = if (mPath.reversed) -1.0 else 1.0
        val leftVelo = negate * (velocityTarget * (2.0 + (curvature * Constants.TRACK_WIDTH)) / 2.0)
        val rightVelo = negate * (velocityTarget * (2.0 - (curvature * Constants.TRACK_WIDTH)) / 2.0)
        return PathFollowerOutput(leftVelo, rightVelo)
    }

    /**
    *   Calcuates the lookahead point based on robot position and desired lookahead distance.
    *   Algorithm basically creates a circle around the robot with radius = lookahead distance
    *   then finds the intersection point with the path line. It then chooses the most suitable point.
    *   If it doesnt find a lookahead point, it sets the lookahead to the last point in the path
    *   @param robotPose current pose of the robot
    *   @return calculated lookahead of the robot as a Vector2
    */
    @Suppress("ComplexMethod")
    private fun calculateLookahead(robotPose: Pose2d): Vector2 {
        mLastClosestPointIndex = mPath.findClosestPointIndex(robotPose, mLastClosestPointIndex)
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
        }
        if (lookahead == null) {
            lookahead = mPath.endPose.translation
        } else {
            val distanceToEnd = robotPose.translation.distanceTo(mPath.endPose.translation)
            if (distanceToEnd < Constants.Path.LOOK_AHEAD_DISTANCE) {
                lookahead = mPath.endPose.translation
            }
        }
        return lookahead
    }

    /**
    * calculates curvature between robot point and lookahead point
    * @param robotPose current pose of the robot
    * @param lookahead lookahead point
    * @param robotAngle the modified robot angle to prevent undefined curvatures
    * @return the curvature of the arc that the robot must follow to reach the lookahead point
    */
    private fun calculateCurvature(robotPose: Pose2d, lookahead: Vector2, robotAngle: Double): Double {
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
    /**
    * @param robotPos position of the robot
    * @return whether the robot is done with the the designated path based on robot location
    */
    fun doneWithPath(robotPose: Pose2d): Boolean {
        val distance = robotPose.translation.distanceTo(mPath.endPose.translation)
        return distance < Constants.Path.LOOK_AHEAD_DISTANCE
    }

    /**
    * resets follower to first point
    */
    fun reset() {
        mLastClosestPointIndex = 0
    }

    /**
    * @property leftVelocity target velocity of the right side of the drivetrain
    * @property rightVelocity target velocity of the right side of the drivetrain
    */
    public data class PathFollowerOutput(val leftVelocity: Double = 0.0, val rightVelocity: Double = 0.0)
}
