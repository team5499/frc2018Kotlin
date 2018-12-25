package frc.team5499.frc2018Kotlin.utils.math.splines

import frc.team5499.frc2018Kotlin.utils.math.geometry.Rotation2d
import frc.team5499.frc2018Kotlin.utils.math.geometry.Vector2
import frc.team5499.frc2018Kotlin.utils.math.geometry.Pose2d
import frc.team5499.frc2018Kotlin.utils.math.geometry.Pose2dWithCurvature

abstract class Spline {

    abstract fun getPoint(t: Double): Vector2

    abstract fun getHeading(t: Double): Rotation2d

    abstract fun getCurvature(t: Double): Double

    abstract fun getDCurvature(t: Double): Double

    abstract fun getVelocity(t: Double): Double

    fun getPose(t: Double): Pose2d {
        return Pose2d(getPoint(t), getHeading(t))
    }

    fun getPoseWithCurvature(t: Double): Pose2dWithCurvature {
        return Pose2dWithCurvature(getPose(t), getCurvature(t), getDCurvature(t))
    }
}
