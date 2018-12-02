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

    private const val kMaxVelocity = 50.0
    private const val kMaxAccel = 50.0

    var pathSet: PathSet? = null
        get() {
            return field
        }
        private set

    fun generatePathSet() {
        if (pathSet == null) {
            pathSet = PathSet()
        }
    }

    @Suppress("LongParameterList", "ComplexMethod")
    private fun generatePath(
        reversed: Boolean,
        waypoints: MutableList<Pose2d>,
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

    private fun generatePath(reversed: Boolean, waypoints: MutableList<Pose2d>): Path {
        return generatePath(reversed, waypoints, kMaxVelocity, kMaxAccel, 10.0, 0.0)
    }

    private val kCenterStartPose = Pose2d(Vector2(0.0, -4.0), Rotation2d.fromDegrees(180.0))
    private val kLeftSwitch = Pose2d(Vector2(100.0, 60.0), Rotation2d.fromDegrees(180.0))
    private val kRightSwitch = Pose2d(Vector2(100, -60), Rotation2d.fromDegrees(180.0))

    public class PathSet {
        public val startLeftSwitchPath: Path
        public val startRightSwitchPath: Path
        init {
            this.startLeftSwitchPath = generateStartLeftSwitch()
            this.startRightSwitchPath = generateStartRightSwitch()
        }

        fun generateStartLeftSwitch(): Path {
            val waypoints: MutableList<Pose2d> = mutableListOf(
                kCenterStartPose,
                kCenterStartPose.transformBy(Vector2(12.0, 0.0)),
                kLeftSwitch.transformBy(Vector2(-12.0, 0.0)),
                kLeftSwitch
            )

            return generatePath(true, waypoints)
        }

        fun generateStartRightSwitch(): Path {
            val waypoints: MutableList<Pose2d> = mutableListOf(
                kCenterStartPose,
                kCenterStartPose.transformBy(Vector2(12.0, 0.0)),
                kRightSwitch.transformBy(Vector2(-12.0, 0.0)),
                kRightSwitch
            )

            return generatePath(true, waypoints)
        }
    }
}
