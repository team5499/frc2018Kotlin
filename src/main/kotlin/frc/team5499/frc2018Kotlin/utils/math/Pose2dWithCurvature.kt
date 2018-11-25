package frc.team5499.frc2018Kotlin.utils.math

class Pose2dWithCurvature(
    translation: Vector2,
    rotation: Rotation2d,
    curvature: Double
) : Pose2d(translation, rotation) {

    val curvature: Double
        get() = field

    init {
        this.curvature = curvature
    }

    constructor(pose: Pose2d, curvature: Double): this(pose.translation, pose.rotation, curvature)

    override fun toString(): String {
        return super.toString() + " - Curvature: $curvature"
    }
}
