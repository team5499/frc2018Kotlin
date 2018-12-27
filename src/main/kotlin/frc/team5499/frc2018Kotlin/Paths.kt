package frc.team5499.frc2018Kotlin

import org.team5499.monkeyLib.path.PathGenerator
import org.team5499.monkeyLib.path.Path
import org.team5499.monkeyLib.math.geometry.Pose2d
import org.team5499.monkeyLib.math.geometry.Vector2
import org.team5499.monkeyLib.math.geometry.Rotation2d

object Paths {

    private val kCenterStartPose = Pose2d(Vector2(0.0, -4.0), Rotation2d.fromDegrees(180.0))
    private val kLeftSwitch = Pose2d(Vector2(100.0, 60.0), Rotation2d.fromDegrees(180.0))
    private val kRightSwitch = Pose2d(Vector2(100, -60), Rotation2d.fromDegrees(180.0))
    private val kPyramid = Pose2d(Vector2(82.0, 5.0), Rotation2d.fromDegrees(0.0))

    public val startLeftSwitch: Path
    public val leftSwitchToPyramid: Path
    public val pyramidToLeftSwitch: Path

    public val startRightSwitch: Path
    public val rightSwitchToPyramid: Path
    public val pyramidToRightSwitch: Path

    init {
        this.startLeftSwitch = generateStartLeftSwitch()
        this.leftSwitchToPyramid = generateLeftSwitchToPyramid()
        this.pyramidToLeftSwitch = generatePyramidToLeftSwitch()

        this.startRightSwitch = generateStartRightSwitch()
        this.rightSwitchToPyramid = generateRightSwitchToPyramid()
        this.pyramidToRightSwitch = generatePyramidToRightSwitch()
    }

    private fun generateStartLeftSwitch(): Path {
        val waypoints: Array<Pose2d> = arrayOf(
            kCenterStartPose,
            kCenterStartPose.transformBy(Vector2(12.0, 0.0)),
            kLeftSwitch.transformBy(Vector2(-12.0, 0.0)),
            kLeftSwitch
        )
        return PathGenerator.generatePath(true, waypoints)
    }

    private fun generateLeftSwitchToPyramid(): Path {
        val waypoints: Array<Pose2d> = arrayOf(
            kLeftSwitch,
            kLeftSwitch.transformBy(Vector2(-12.0, 0.0)),
            kPyramid.transformBy(Vector2(-12.0, 0.0)),
            kPyramid
        )
        return PathGenerator.generatePath(false, waypoints, Constants.Path.MAX_VELOCITY,
            Constants.Path.MAX_ACCELERATION, 15.0, 0.0)
    }

    private fun generatePyramidToLeftSwitch(): Path {
        val waypoints: Array<Pose2d> = arrayOf(
            kPyramid,
            kPyramid.transformBy(Vector2(-12.0, 0.0)),
            kLeftSwitch.transformBy(Vector2(-12.0, 0.0)),
            kLeftSwitch
        )
        return PathGenerator.generatePath(true, waypoints)
    }

    private fun generateStartRightSwitch(): Path {
        val waypoints: Array<Pose2d> = arrayOf(
            kCenterStartPose,
            kCenterStartPose.transformBy(Vector2(12.0, 0.0)),
            kRightSwitch.transformBy(Vector2(-12.0, 0.0)),
            kRightSwitch
        )

        return PathGenerator.generatePath(true, waypoints)
    }

    private fun generateRightSwitchToPyramid(): Path {
        val waypoints: Array<Pose2d> = arrayOf(
            kRightSwitch,
            kRightSwitch.transformBy(Vector2(-12.0, 0.0)),
            kPyramid.transformBy(Vector2(-12.0, 0.0)),
            kPyramid
        )
        return PathGenerator.generatePath(false, waypoints, Constants.Path.MAX_VELOCITY,
            Constants.Path.MAX_ACCELERATION, 15.0, 0.0)
    }

    private fun generatePyramidToRightSwitch(): Path {
        val waypoints: Array<Pose2d> = arrayOf(
            kPyramid,
            kPyramid.transformBy(Pose2d(Vector2(-12.0, -30.0), Rotation2d.fromDegrees(90))),
            kRightSwitch
        )
        return PathGenerator.generatePath(true, waypoints)
    }
}
