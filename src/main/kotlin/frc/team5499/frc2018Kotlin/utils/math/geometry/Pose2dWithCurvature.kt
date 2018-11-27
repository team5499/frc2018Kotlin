package frc.team5499.frc2018Kotlin.utils.math.geometry

import frc.team5499.frc2018Kotlin.utils.Utils

class Pose2dWithCurvature(
    translation: Vector2,
    rotation: Rotation2d,
    curvature: Double,
    dCurvature: Double = 0.0
) : Geometric<Pose2dWithCurvature> {

    val curvature: Double
        get() = field
    val dCurvature: Double
        get() = field
    val pose: Pose2d
        get() = field

    init {
        this.curvature = curvature
        this.dCurvature = dCurvature
        this.pose = Pose2d(translation, rotation)
    }

    constructor(pose: Pose2d, curvature: Double, dCurvature: Double):
        this(pose.translation, pose.rotation, curvature, dCurvature = 0.0)
    constructor(): this(Vector2(), Rotation2d(), 0.0, 0.0)

    override fun interpolate(other: Pose2dWithCurvature, x: Double): Pose2dWithCurvature {
        return Pose2dWithCurvature(pose.interpolate(other.pose, x),
            Utils.interpolate(curvature, other.curvature, x),
            Utils.interpolate(dCurvature, other.dCurvature, x)
        )
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Pose2dWithCurvature) return false
        return pose.equals(other.pose) && curvature == other.curvature && dCurvature == other.dCurvature
    }

    override fun toString(): String {
        return pose.toString() + " - Curvature: $curvature"
    }

    override fun toCSV() = "${pose.toCSV()},$curvature,$dCurvature"

    override fun hashCode() = super.hashCode()
}
