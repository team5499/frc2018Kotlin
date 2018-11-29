package frc.team5499.frc2018Kotlin.path

import frc.team5499.frc2018Kotlin.utils.math.geometry.Pose2d
import frc.team5499.frc2018Kotlin.utils.math.geometry.Vector2
import frc.team5499.frc2018Kotlin.utils.math.geometry.Rotation2d

@SuppressWarnings("MagicNumber")
object PathGenerator {

    val PATHSET by lazy {
        println("Generating paths...")
        val startTime = System.currentTimeMillis()
        PathSet()
        println("Paths generated in ${System.currentTimeMillis() - startTime}ms")
    }

    private fun generatePath(
        reversed: Boolean,
        waypoints: MutableList<Pose2d>,
        maxVelo: Double,
        maxAccel: Double
    ) {
    }

    private val startPose = Pose2d(Vector2(0.0, 0.0), Rotation2d.fromDegrees(180.0))

    class PathSet {
        init {
            // something
        }
    }
}
