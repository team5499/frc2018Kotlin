package tests.utils.math.splines

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

import frc.team5499.frc2018Kotlin.utils.math.geometry.Vector2
import frc.team5499.frc2018Kotlin.utils.math.geometry.Rotation2d
import frc.team5499.frc2018Kotlin.utils.math.geometry.Pose2d

import frc.team5499.frc2018Kotlin.utils.math.splines.QuinticHermiteSpline

class QuinticHermiteOptimizerTest {

    @Suppress("LongMethod")
    @Test
    fun testOptimizer() {
        // test porabola thing
        val t = QuinticHermiteSpline.fitParabola(Vector2(-1.0, 0.015), Vector2(0.0, 0.015), Vector2(1.0, 0.016))
        assertEquals(t, -0.5, 0.1)

        val a = Pose2d(Vector2(0, 100), Rotation2d.fromDegrees(270))
        val b = Pose2d(Vector2(50, 0), Rotation2d.fromDegrees(0))
        val c = Pose2d(Vector2(100, 100), Rotation2d.fromDegrees(90))

        val d = Pose2d(Vector2(0, 0), Rotation2d.fromDegrees(90))
        val e = Pose2d(Vector2(0, 50), Rotation2d.fromDegrees(0))
        val f = Pose2d(Vector2(100, 0), Rotation2d.fromDegrees(90))
        val g = Pose2d(Vector2(100, 100), Rotation2d.fromDegrees(0))

        val h = Pose2d(Vector2(0, 0), Rotation2d.fromDegrees(0))
        val i = Pose2d(Vector2(50, 0), Rotation2d.fromDegrees(0))
        val j = Pose2d(Vector2(100, 50), Rotation2d.fromDegrees(45))
        val k = Pose2d(Vector2(150, 0), Rotation2d.fromDegrees(270))
        val l = Pose2d(Vector2(150, -50), Rotation2d.fromDegrees(270))

        var startTime = System.currentTimeMillis()

        val s0 = QuinticHermiteSpline(a, b)
        println("Took ${System.currentTimeMillis() - startTime} ms to generate 1 splines")
        val s1 = QuinticHermiteSpline(b, c)

        val s2 = QuinticHermiteSpline(d, e)
        val s3 = QuinticHermiteSpline(e, f)
        val s4 = QuinticHermiteSpline(f, g)

        val s5 = QuinticHermiteSpline(h, i)
        val s6 = QuinticHermiteSpline(i, j)
        val s7 = QuinticHermiteSpline(j, k)
        val s8 = QuinticHermiteSpline(k, l)

        println("Took ${System.currentTimeMillis() - startTime} ms to generate 9 splines\n")

        val splines = mutableListOf<QuinticHermiteSpline>()
        splines.add(s0)
        splines.add(s1)
        val splines1 = mutableListOf<QuinticHermiteSpline>()
        splines1.add(s2)
        splines1.add(s3)
        splines1.add(s4)
        val splines2 = mutableListOf<QuinticHermiteSpline>()
        splines2.add(s5)
        splines2.add(s6)
        splines2.add(s7)
        splines2.add(s8)

        // 2 splines
        startTime = System.currentTimeMillis()
        var temp = QuinticHermiteSpline.optimizeSpline(splines)
        println("3 Spline Optimization time (ms): ${System.currentTimeMillis() - startTime}")
        println("Optimized dCurvature by $temp\n")
        assertTrue(temp < 0.014)

        // 3 splines
        startTime = System.currentTimeMillis()
        temp = QuinticHermiteSpline.optimizeSpline(splines1)
        println("4 Spline Optimization time (ms): ${System.currentTimeMillis() - startTime}")
        println("Optimized dCurvature by $temp\n")
        assertTrue(temp < 0.16)

        // 4 splines
        startTime = System.currentTimeMillis()
        temp = QuinticHermiteSpline.optimizeSpline(splines2)
        println("5 Spline Optimization time (ms): ${System.currentTimeMillis() - startTime}")
        println("Optimized dCurvature by $temp\n")
        assertTrue(temp < 0.05)
    }
}
