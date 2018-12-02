package frc.team5499.frc2018Kotlin.path

import frc.team5499.frc2018Kotlin.Constants
import frc.team5499.frc2018Kotlin.utils.Vector2

@SuppressWarnings("MagicNumber")

public class PathFollower(path: Path) {

    private var mPath: Path
    private var mLastClosestPointIndex: Int

    init {
        mPath = path
        mLastClosestPointIndex = 0
    }

    @Suppress("ComplexMethod")
    private fun calculateLookahead(robotPos: Vector2, robotAngle: Double): Vector2 {
        mLastClosestPointIndex = mPath.findClosestPointIndex(robotPos, mLastClosestPointIndex)
        var lookahead: Vector2? = null
        for (i in mLastClosestPointIndex..mPath.getCoordinatesLength() - 2) {
            var begin: Vector2 = mPath.getPoint(i)
            var end: Vector2 = mPath.getPoint(i + 1)
            var d: Vector2 = end - begin
            var f: Vector2 = begin - robotPos
            var a: Double = d.dot(d)
            var b: Double = 2.0 * d.dot(f)
            var c: Double = f.dot(f) - Math.pow(Constants.LOOK_AHEAD_DISTANCE, 2.0)

            var dis: Double = (b * b) - (4.0 * a * c)
            if (dis < 0) {
                continue
            } else {
                dis = Math.sqrt(dis)
                var t1: Double = (-b - dis) / (2.0 * a)
                var t2: Double = (-b + dis) / (2.0 * a)

                if (t1 >= 0 && t1 <= 1) {
                    var temp: Vector2 = d * t1
                    lookahead = begin + temp
                    break
                } else if (t2 >= 0 && t2 <= 1) {
                    var temp: Vector2 = d * t2
                    lookahead = begin + temp
                    break
                }
            }
        }

        if (lookahead == null) {
            lookahead = mPath.getPoint(mPath.getCoordinatesLength() - 1)
        } else {
            var distanceBetweenRobotEnd: Double = Vector2.distanceBetween(
                robotPos, mPath.getPoint(mPath.getCoordinatesLength() - 1))
            if (distanceBetweenRobotEnd < Constants.LOOK_AHEAD_DISTANCE) {
                lookahead = mPath.getPoint(mPath.getCoordinatesLength() - 1)
            }
        }
        return lookahead
    }

    private fun calculateCurvature(
        robotPos: Vector2,
        lookAhead: Vector2,
        robotAngle: Double,
        angleToLookahead: Double
    ): Double {
        var a: Double = (1 / Math.tan(robotAngle))
        var b: Double = -1.0
        var c: Double = -(1 / Math.tan(robotAngle)) * robotPos.y + robotPos.x

        var x: Double = Math.abs(a * lookAhead.y + b * lookAhead.x + c) / (Math.sqrt(a * a + b * b))
        var curvature: Double = (2.0 * x) / (Math.pow(Constants.LOOK_AHEAD_DISTANCE, 2.0))

        var side: Double = Math.signum(Math.sin(robotAngle) * (lookAhead.x - robotPos.x) -
            Math.cos(robotAngle) * (lookAhead.y - robotPos.y))

        return if (angleToLookahead <= 90.0) curvature * side else -curvature * side
    }

    fun update(robotPos: Vector2, newRobotAngle: Double): PathFollowerOutput {
        var robotAngle: Double = newRobotAngle
        robotAngle = Math.toRadians(robotAngle)
        if (robotAngle.equals(0)) { robotAngle = Constants.EPSILON }

        var lookahead: Vector2 = calculateLookahead(robotPos, robotAngle)

        var robotToLookahead: Vector2 = lookahead - robotPos

        var robotDirection: Vector2 = Vector2.representHeadingWithUnitVector(
            -Math.toDegrees(robotAngle) + 90)
        var angleToLookahead: Double = Math.abs(Vector2.angleBetween(robotToLookahead, robotDirection))

        var curvature: Double = calculateCurvature(robotPos, lookahead, robotAngle, angleToLookahead)

        var averageVelocity: Double =
        if (angleToLookahead <= 90.0)
        mPath.getPointVelocity(mLastClosestPointIndex)
        else -mPath.getPointVelocity(mLastClosestPointIndex)

        val leftOutput: Double = averageVelocity * (2.0 + (curvature * Constants.TRACK_WIDTH)) / 2.0
        val rightOutput: Double = averageVelocity * (2.0 - (curvature * Constants.TRACK_WIDTH)) / 2.0

        return PathFollowerOutput(leftOutput, rightOutput)
    }
    public data class PathFollowerOutput(val leftVelocity: Double, val rightVelocity: Double)

    public fun doneWithPath(robotPos: Vector2): Boolean {
        var distanceBetweenRobotEnd: Double = Vector2.distanceBetween(
            robotPos, mPath.getPoint(mPath.getCoordinatesLength() - 1))
        return distanceBetweenRobotEnd < Constants.LOOK_AHEAD_DISTANCE
    }

    public fun reset() {
        mLastClosestPointIndex = 0
    }
}
