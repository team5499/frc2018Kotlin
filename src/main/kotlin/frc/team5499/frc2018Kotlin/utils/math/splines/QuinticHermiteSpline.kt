package frc.team5499.frc2018Kotlin.utils.math.splines

import frc.team5499.frc2018Kotlin.utils.math.Vector2
import frc.team5499.frc2018Kotlin.utils.math.Rotation2d

class QuinticHermiteSpline(p0: Vector2, h0: Rotation2d, p1: Vector2, h1: Rotation2d) : Spline() {

    override fun getPoint(t: Double): Vector2 {
        return Vector2()
    }

    override fun getHeading(t: Double): Rotation2d {
        return Rotation2d()
    }

    override fun getCurvature(t: Double): Double {
        return 0.0
    }
}
