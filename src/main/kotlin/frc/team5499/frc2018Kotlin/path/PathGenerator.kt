package frc.team5499.frc2018Kotlin.path

import frc.team5499.frc2018Kotlin.utils.Vector2

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
        // need to add this
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

    private fun generateSmoothPath(initialPoints: MutableList<Vector2>):
        Pair<MutableList<Vector2>, MutableList<Double>>
    {

        return Pair(initialPoints, mutableListOf(0.0, 0.0))
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
