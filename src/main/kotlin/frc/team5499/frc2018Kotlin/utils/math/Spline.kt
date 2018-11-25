package frc.team5499.frc2018Kotlin.utils.math

abstract class Spline {

    abstract fun getPoint(t: Double): Vector2

    abstract fun getHeading(t: Double): Rotation2d

    abstract fun getCurvature(t: Double): Double

    fun getPose(t: Double): Pair<Vector2, Rotation2d> {
        return Pair(getPoint(t), getHeading(t))
    }
}
