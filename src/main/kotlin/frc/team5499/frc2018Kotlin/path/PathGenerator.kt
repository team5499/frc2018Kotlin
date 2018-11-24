package frc.team5499.frc2018Kotlin.path

import frc.team5499.frc2018Kotlin.utils.Vector2
import frc.team5499.frc2018Kotlin.Constants

@SuppressWarnings("MagicNumber")
object PathGenerator {

    enum class PathType {
        QUINTIC_SPLINE,
        CUBIC_SPLINE,
        SMOOTH_POINTS
    }

    // function for generating path
    // can have multiple methods for this. Maybe using enum
    // quintic splines, cubic splines, method we are currently using, etc
    fun generatePath(
        initialPoints: MutableList<Vector2>,
        reversed: Boolean,
        type: PathType = PathType.SMOOTH_POINTS
    ): Path {
        if (initialPoints.size < 2) throw IllegalArgumentException("Need >= 2 points to create a valid path")
            val path: MutableList<Vector2>
            when (type) {
                PathType.SMOOTH_POINTS -> {
                    path = smoothPath(initialPoints)
                }
                else -> {
                    println("ERROR: SPLINES NOT IMPLIMENTED YET. USING SMOOTH BOIS INSTEAD")
                    path = smoothPath(initialPoints)
                }
            }

                // calculate curvature
        var curvatures: MutableList<Double> = mutableListOf()
        curvatures.add(0.0)
        for (i in 1..(path.size - 2)) {
            // P
            var x1 = path.get(i + 0).x
            val y1 = path.get(i + 0).y
            // Q
            val x2 = path.get(i - 1).x
            val y2 = path.get(i - 1).y
            // R
            val x3 = path.get(i + 1).x
            val y3 = path.get(i + 1).y

            if (x1 == x2) x1 += Constants.EPSILON
            val k1 = 0.5 * (x1 * x1 + y1 * y1 - x2 * x2 - y2 * y2) / (x1 - x2)
            val k2 = (y1 - y2) / (x1 - x2)
            var b = 0.5 * (x2 * x2 - 2.0 * x2 * k1 + y2 * y2 - x3 * x3 + 2.0 * x3 * k1 - y3 * y3)
            b /= (x3 * k2 - y3 + y2 - x2 * k2)
            val a = k2 - k1 * b
            val r = Math.hypot((x1 - a), (y1 - b))
            curvatures.add(1 / r)
        }
        curvatures.add(0.0)
        // println("Points: ${path.size}")
        // println("Curvatures: $curvatures")

        var velocities: MutableList<Double> = mutableListOf()
        // max velo
        for (i in curvatures.indices) {
            velocities.add(Math.min(Constants.Path.MAX_VELOCITY, 3.0 / curvatures[i]))
        }
        // println("Velo 1: $velocities")

        // limited velo
        velocities[velocities.size - 1] = 0.0
        for (i in ((velocities.size - 2).downTo(0))) {
            val distance = path.get(i).distanceTo(path.get(i + 1))
            velocities[i] = Math.min(velocities[i], Math.sqrt(Math.pow(velocities[i + 1], 2.0) + 2 *
                Constants.Path.MAX_ACCELERATION * distance))
        }
        // println("Velo 2: $velocities")

        return Path(path, velocities, reversed)
    }

    private fun smoothPath(initialPoints: MutableList<Vector2>):
        MutableList<Vector2>
    {
        // println("Initial points: $initialPoints")
        var path: MutableList<Vector2> = mutableListOf()
        // inject points
        for (i in 0..(initialPoints.size - 2)) {
            val start = initialPoints.get(i)
            val end = initialPoints.get(i + 1)
            var segment = end - start
            // println(segment)
            val numberOfFittingPoints = Math.ceil(segment.magnitude / Constants.Path.SMOOTH_POINT_SPACING).toInt()
            segment = segment.normalized * Constants.Path.SMOOTH_POINT_SPACING
            for (j in 0..numberOfFittingPoints) {
                path.add(start + (segment * j))
            }
        }
        path.add(Vector2(initialPoints.get(initialPoints.size - 1)))
        // println("Path before smoothing: $path")

        var change = Constants.Path.SMOOTHING_TOLERANCE
        var newPath = path.toMutableList()
        while (change >= Constants.Path.SMOOTHING_TOLERANCE) {
            change = 0.0
            for (i in 1..path.size - 2) {
                val aux1 = newPath.get(i).x
                val dx = ((1.0 - Constants.Path.CURVE_VAL) * (path.get(i).x - newPath.get(i).x) +
                    Constants.Path.CURVE_VAL * (newPath.get(i - 1).x + newPath.get(i + 1).x - (2.0 * newPath.get(i).x)))
                val aux2 = newPath.get(i).y
                val dy = ((1.0 - Constants.Path.CURVE_VAL) * (path.get(i).y - newPath.get(i).y) +
                    Constants.Path.CURVE_VAL * (newPath.get(i - 1).y + newPath.get(i + 1).y - (2.0 * newPath.get(i).y)))
                newPath.set(i, newPath.get(i).translateBy(dx, dy))
                change += Math.abs(aux1 - newPath.get(i).x) + Math.abs(aux2 - newPath.get(i).y)
            }
        }

        path = newPath.toMutableList()
        // println("Path after smoothing: $newPath")

        // extend last point by lookahead distance
        val lastSegment = (path.get(path.size - 1) - path.get(path.size - 2)).normalized
        path[path.size - 1] = path[path.size - 1] + lastSegment * Constants.Path.LOOK_AHEAD_DISTANCE

        // println("final points: $path")
        // println(lastSegment)

        return path
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
