package frc.team5499.frc2018Kotlin.path

import frc.team5499.frc2018Kotlin.utils.Vector2
import frc.team5499.frc2018Kotlin.Constants

@SuppressWarnings("MagicNumber")
object PathGenerator {

    private enum class PathType {
        QUINTIC_SPLINE,
        CUBIC_SPLINE,
        SMOOTH_POINTS // idk what else to call this. Just the normal smoothing of points
    }

    // function for generating path
    // can have multiple methods for this. Maybe using enum
    // quintic splines, cubic splines, method we are currently using, etc
    private fun generatePath(
        initialPoints: MutableList<Vector2>,
        reversed: Boolean,
        type: PathType = PathType.SMOOTH_POINTS
    ): Path {
        if (initialPoints.size < 2) throw IllegalArgumentException("Need >= 2 points to create a valid path")
        when (type) {
            PathType.SMOOTH_POINTS -> {
                val temp = generateSmoothPath(initialPoints)
                return Path(temp.first, temp.second, reversed)
            }
            else -> {
                // in the future, this method will throw an error
                println("These methods have not been implimented yet. Using supported method!")
                val temp = generateSmoothPath(initialPoints)
                return Path(temp.first, temp.second, reversed)
            }
        }
    }

    fun generateSmoothPath(initialPoints: MutableList<Vector2>):
        Pair<MutableList<Vector2>, MutableList<Double>>
    {
        var newPoints: MutableList<Vector2> = mutableListOf()
        // inject points
        for (i in 0..(initialPoints.size - 2)) {
            val start = initialPoints.get(i)
            val end = initialPoints.get(i + 1)
            var segment = end - start
            val numberOfFittingPoints = Math.ceil(segment.magnitude / Constants.Path.SMOOTH_POINT_SPACING).toInt()
            segment = segment.normalized * Constants.Path.SMOOTH_POINT_SPACING
            for (j in 0..numberOfFittingPoints) {
                newPoints.add(start + (segment * j))
            }
        }
        newPoints.add(Vector2(initialPoints.get(initialPoints.size - 1)))

        // smooth points
            var tempPoints: MutableList<Vector2> = newPoints
            var change = Constants.Path.SMOOTHING_TOLERANCE

            while (change >= Constants.Path.SMOOTHING_TOLERANCE) {
                change = 0.0
                for (i in 1..newPoints.size - 2) {
                    val aux1 = tempPoints.get(i).x
                    val x = Constants.Path.CURVE_VAL * (newPoints.get(i).x - aux1)
                        + (1 - Constants.Path.CURVE_VAL) * (tempPoints.get(i - 1).x +
                        tempPoints.get(i + 1).x - (2.0 * aux1))
                    tempPoints[i] = Vector2(x, tempPoints.get(i).y)
                    change += Math.abs(aux1 - tempPoints.get(i).x)

                    val aux2 = tempPoints.get(i).y
                    val y = Constants.Path.CURVE_VAL * (newPoints.get(i).y - aux2)
                        + (1 - Constants.Path.CURVE_VAL) * (tempPoints.get(i - 1).y +
                        tempPoints.get(i + 1).y - (2.0 * aux2))
                    tempPoints[i] = Vector2(tempPoints.get(i).x, y)
                    change += Math.abs(aux2 - tempPoints.get(i).y)
                }
            }
            newPoints = tempPoints

        // extend last point by lookahead distance
        val lastSegment = (newPoints.get(newPoints.size - 1) - newPoints.get(newPoints.size - 2)).normalized
        newPoints[newPoints.size - 1] = newPoints[newPoints.size - 1] + lastSegment * Constants.Path.LOOK_AHEAD_DISTANCE

        // calculate curvature
        var curvatures: MutableList<Double> = mutableListOf()
        curvatures.add(0.0)
        for (i in 1..(newPoints.size - 2)) {
            // P
            var x1 = newPoints.get(i + 0).x
            val y1 = newPoints.get(i + 0).y
            // Q
            val x2 = newPoints.get(i - 1).x
            val y2 = newPoints.get(i - 1).y
            // R
            val x3 = newPoints.get(i + 1).x
            val y3 = newPoints.get(i + 1).y

            if (x1 == x2) x1 += Constants.EPSILON
            val k1 = 0.5 * (x1 * x1 + y1 * y1 - x2 * x2 - y2 * y2) / (x1 - x2)
            val k2 = (y1 - y2) / (x1 - x2)
            var b = 0.5 * (x2 * x2 - 2 * x2 * k1 + y2 * y2 - x3 * x3 + 2.0 * x3 * k1 - y3 * y3)
            b /= (x3 * k2 - y3 + y2 - x2 * k2)
            val a = k2 - k1 * b
            val r = Math.hypot((x1 - a), (y1 - b))
            curvatures.add(1 / r)
        }
        curvatures.add(0.0)
        // println("Points: ${newPoints.size}")
        // println("Curvatures: ${curvatures.size}")

        var velocities: MutableList<Double> = mutableListOf()
        // max velo
        for (i in curvatures.indices) {
            velocities.add(Math.min(Constants.Path.MAX_VELOCITY, 3.0 / curvatures[i]))
        }

        // limited velo
        for (i in ((velocities.size - 2).downTo(0))) {
            val distance = newPoints.get(i).distanceTo(newPoints.get(i + 1))
            velocities[i] = Math.min(velocities[i], Math.sqrt(Math.pow(velocities[i + 1], 2.0) + 2 *
                Constants.Path.MAX_ACCELERATION * distance))
        }

        return Pair(newPoints, velocities)
    }

    // points
    private val startingPointCenter = Vector2(0.0, -4.0)
    private val leftSwitch = Vector2(100.0, 60.0)
    private val rightSwitch = Vector2(100.0, -60.0)
    private val pyramidCube1Position = Vector2(70.0, 0.0) // cube on point

    // paths
    object Paths {
        // dont know if to make these "by lazy" or not. Maybe not a good idea
        // because we want them loading before auto, not during it
        val centerToSwitchLeft: Path
            get() {
                val list: MutableList<Vector2> = mutableListOf(
                    startingPointCenter,
                    startingPointCenter.translateBy(18.0, 0.0),
                    leftSwitch.translateBy(-20.0, 0.0),
                    leftSwitch
                )
                return generatePath(list, true)
            }

        val centerToSwitchRight: Path
            get() {
                val list: MutableList<Vector2> = mutableListOf(
                    startingPointCenter,
                    startingPointCenter.translateBy(18.0, 0.0),
                    rightSwitch.translateBy(-20.0, 0.0),
                    rightSwitch
                )
                return generatePath(list, true)
            }

        val leftSwitchToPyramid: Path
            get() {
                val list: MutableList<Vector2> = mutableListOf(
                    leftSwitch,
                    leftSwitch.translateBy(-24.0, 0.0),
                    pyramidCube1Position.translateBy(-4.0, 10.0),
                    pyramidCube1Position
                )
                return generatePath(list, false)
            }

        val rightSwitchToPyramid: Path
            get() {
                val list: MutableList<Vector2> = mutableListOf(
                    rightSwitch,
                    rightSwitch.translateBy(-24.0, 0.0),
                    pyramidCube1Position.translateBy(-4.0, -10.0),
                    pyramidCube1Position
                )
                return generatePath(list, false)
            }
    }
}
