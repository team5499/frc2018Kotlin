package tests.utils

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions

import frc.team5499.frc2018Kotlin.utils.Vector2

public class Vector2Test {
    @Test
    fun test_dot() {
        val a = Vector2(3, 4)
        val b = Vector2(5, 6)
        Assertions.assertEquals(a.dot(b), 39.0, 1E-5)
    }

    @Test
    fun test_magnitude() {
        val a = Vector2(3, 4)
        Assertions.assertEquals(a.magnitude, 5.0, 1E-5)
    }

    @Test
    fun test_distance_between() {
        val a = Vector2(0, 0)
        val b = Vector2(3, 4)
        Assertions.assertEquals(a.distanceTo(b), 5.0, 1E-5)
    }

    @Test
    fun test_angle() {
        val a = Vector2(0.5, Math.sqrt(3.0) / 2.0)
        Assertions.assertEquals(a.angle, Math.toRadians(60.0), 1E-5)
    }

    @Test
    fun test_normalized() {
        val a = Vector2(3, 4).normalized
        Assertions.assertEquals(a.x, 3.0 / 5.0, 1E-5)
        Assertions.assertEquals(a.y, 4.0 / 5.0, 1E-5)
    }

    @Test
    fun test_zero() {
        val z = Vector2.Zero
        Assertions.assertEquals(z.x, 0.0, 1E-5)
        Assertions.assertEquals(z.y, 0.0, 1E-5)
    }
}
