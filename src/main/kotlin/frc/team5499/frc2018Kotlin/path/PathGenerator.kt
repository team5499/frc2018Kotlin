package frc.team5499.frc2018Kotlin.path

import frc.team5499.frc2018Kotlin.utils.math.geometry.Pose2dWithCurvature
import frc.team5499.frc2018Kotlin.utils.math.geometry.Pose2d
import frc.team5499.frc2018Kotlin.utils.math.geometry.Vector2
import frc.team5499.frc2018Kotlin.utils.math.geometry.Rotation2d

import frc.team5499.frc2018Kotlin.Constants

import frc.team5499.frc2018Kotlin.utils.math.splines.SplineGenerator
import frc.team5499.frc2018Kotlin.utils.math.splines.QuinticHermiteSpline

@SuppressWarnings("MagicNumber")
object PathGenerator {

    private val kCenterStartPose = Pose2d(Vector2(0.0, -4.0), Rotation2d.fromDegrees(180.0))
    private val kLeftSwitch = Pose2d(Vector2(100.0, 60.0), Rotation2d.fromDegrees(180.0))
    private val kRightSwitch = Pose2d(Vector2(100, -60), Rotation2d.fromDegrees(180.0))
    private val kPyramid = Pose2d(Vector2(82.0, 5.0), Rotation2d.fromDegrees(0.0))

    // THE PATHS
    public object Paths {

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
            return generatePath(true, waypoints)
        }

        private fun generateLeftSwitchToPyramid(): Path {
            val waypoints: Array<Pose2d> = arrayOf(
                kLeftSwitch,
                kLeftSwitch.transformBy(Vector2(-12.0, 0.0)),
                kPyramid.transformBy(Vector2(-12.0, 0.0)),
                kPyramid
            )
            return generatePath(false, waypoints, Constants.Path.MAX_VELOCITY,
                Constants.Path.MAX_ACCELERATION, 15.0, 0.0)
        }

        private fun generatePyramidToLeftSwitch(): Path {
            val waypoints: Array<Pose2d> = arrayOf(
                kPyramid,
                kPyramid.transformBy(Vector2(-12.0, 0.0)),
                kLeftSwitch.transformBy(Vector2(-12.0, 0.0)),
                kLeftSwitch
            )
            return generatePath(true, waypoints)
        }

        private fun generateStartRightSwitch(): Path {
            val waypoints: Array<Pose2d> = arrayOf(
                kCenterStartPose,
                kCenterStartPose.transformBy(Vector2(12.0, 0.0)),
                kRightSwitch.transformBy(Vector2(-12.0, 0.0)),
                kRightSwitch
            )

            return generatePath(true, waypoints)
        }

        private fun generateRightSwitchToPyramid(): Path {
            val waypoints: Array<Pose2d> = arrayOf(
                kRightSwitch,
                kRightSwitch.transformBy(Vector2(-12.0, 0.0)),
                kPyramid.transformBy(Vector2(-12.0, 0.0)),
                kPyramid
            )
            return generatePath(false, waypoints, Constants.Path.MAX_VELOCITY,
                Constants.Path.MAX_ACCELERATION, 15.0, 0.0)
        }

        private fun generatePyramidToRightSwitch(): Path {
            val waypoints: Array<Pose2d> = arrayOf(
                kPyramid,
                kPyramid.transformBy(Vector2(-12.0, 0.0)),
                kRightSwitch.transformBy(Vector2(-12.0, 0.0)),
                kRightSwitch
            )
            return generatePath(true, waypoints)
        }
    }

    @Suppress("LongParameterList", "ComplexMethod")
    private fun generatePath(
        reversed: Boolean,
        waypoints: Array<Pose2d>,
        maxVelo: Double,
        maxAccel: Double,
        startVelo: Double,
        endVelo: Double
    ): Path {
        val waypointsMaybeFlipped = waypoints.toMutableList()
        val flip = Pose2d.fromRotation(Rotation2d(-1.0, 0.0, false))
        if (reversed) {
            waypointsMaybeFlipped.clear()
            for (pose in waypoints) {
                waypointsMaybeFlipped.add(pose.transformBy(flip))
            }
        }

        val splines: MutableList<QuinticHermiteSpline> = mutableListOf()
        for (i in 0..waypointsMaybeFlipped.size - 2) {
            splines.add(QuinticHermiteSpline(waypointsMaybeFlipped.get(i), waypointsMaybeFlipped.get(i + 1)))
        }
        QuinticHermiteSpline.optimizeSpline(splines)

        var samples = SplineGenerator.parameterizeSplines(splines)
        if (reversed) {
            val flipped = samples.toMutableList()
            flipped.clear()
            for (i in 0..samples.size - 1) {
                flipped.add(Pose2dWithCurvature(
                    samples.get(i).pose.transformBy(flip),
                    -samples.get(i).curvature,
                    samples.get(i).dCurvature
                ))
            }
            samples = flipped
        }

        // extend last segment by lookahead distance
        val lastNorm = (samples.get(samples.size - 1).translation -
            samples.get(samples.size - 2).translation).normalized
        val newSegment = samples.get(samples.size - 1).translation + (lastNorm * Constants.Path.LOOK_AHEAD_DISTANCE)
        samples.set(samples.size - 1, Pose2dWithCurvature(
            Pose2d(newSegment, samples.get(samples.size - 1).rotation),
            samples.get(samples.size - 1).curvature,
            samples.get(samples.size - 1).dCurvature)
        )

        val velocities = mutableListOf<Double>()
        for (i in 0..samples.size - 1) {
            velocities.add(Math.min(maxVelo, Math.abs(3.0 / samples.get(i).curvature)))
        }
        velocities.set(velocities.size - 1, endVelo)
        for (i in (samples.size - 2).downTo(0)) {
            val distance = samples.get(i).translation.distanceTo(samples.get(i + 1).translation)
            val value = Math.min(
                velocities.get(i),
                Math.sqrt(Math.pow(velocities.get(i + 1), 2.0) + 2.0 * maxAccel * distance)
            )
            velocities.set(i, value)
        }

        velocities.set(0, startVelo)
        for (i in 0..samples.size - 2) {
            val distance = samples.get(i).translation.distanceTo(samples.get(i + 1).translation)
            val value = Math.sqrt(Math.pow(velocities.get(i), 2.0) + 2.0 * maxAccel * distance)
            if (value < velocities.get(i + 1))
                velocities.set(i + 1, value)
        }

        return Path(samples, velocities, reversed)
    }

    private fun generatePath(reversed: Boolean, waypoints: Array<Pose2d>): Path {
        return generatePath(
            reversed, waypoints,
            Constants.Path.MAX_VELOCITY,
            Constants.Path.MAX_ACCELERATION,
            Constants.Path.DEFAULT_START_VELOCITY,
            Constants.Path.DEFAULT_END_VELOCITY
        )
    }
}
