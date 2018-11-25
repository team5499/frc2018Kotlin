package tests.utils.math.splines

import org.junit.jupiter.api.Test

import frc.team5499.frc2018Kotlin.utils.math.Vector2
import frc.team5499.frc2018Kotlin.utils.math.Rotation2d

import frc.team5499.frc2018Kotlin.utils.math.splines.CubicHermiteSpline
import frc.team5499.frc2018Kotlin.utils.math.splines.SplineGenerator

class SplineGeneratorTest {

    @Test
    fun test_generator() {
        val s0 = CubicHermiteSpline(Vector2(0, 0), Rotation2d.fromDegrees(0.0), Vector2(20, 20), Rotation2d.fromDegrees(90.0))
        // val s1 = CubicHermiteSpline(Vector2(20, 20), Rotation2d.fromDegrees(90.0), Vector2(100, -30), Rotation2d.fromDegrees(180.0))
        val vals = SplineGenerator.parameterizeSpline(s0)
    }
}
