package frc.team5499.frc2018Kotlin.path

import frc.team5499.frc2018Kotlin.utils.Vector2

@SuppressWarnings("MagicNumber")
object PathGenerator {

    // function for generating path
    // can have multiple methods for this. Maybe using enum
    // quintic splines, cubic splines, method we are currently using, etc
    private fun generatePath(initialPoints: MutableList<Vector2>, reversed: Boolean): Path {
        // need to add this
        return Path(mutableListOf(Vector2(0, 0), Vector2(0, 0)), mutableListOf(0.0, 0.0), false)
    }

    // points
    private val startingPointCenter = Vector2(0.0, -4.0)
    private val leftSwitch = Vector2(100.0, 60.0)
    private val rightSwitch = Vector2(100.0, -60.0)
    private val pyramidCube1Position = Vector2(70.0, 0.0) // cube on point

    // paths
    object Paths {
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
