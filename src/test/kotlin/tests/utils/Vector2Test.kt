package tests.utils

import org.junit.Test
import org.junit.Assert

import frc.team5499.frc2018Kotlin.utils.Vector2

public class Vector2Test {
    @Test
    fun test_dot() {
        val a = Vector2(3, 4)
        val b = Vector2(5, 6)
        Assert.assertEquals(a.dot(b), 39.0, 1E-5)
    }

    @Test
    fun test_magnitude() {
        val a = Vector2(3, 4)
        Assert.assertEquals(a.magnitude, 5.0, 1E-5)
    }

    @Test
    fun test_distance_between() {
        val a = Vector2(0, 0)
        val b = Vector2(3, 4)
        Assert.assertEquals(a.distanceTo(b), 5.0, 1E-5)
        Assert.assertEquals(Vector2.distanceBetween(a, b), 5.0, 1E-5)
    }

    @Test
    fun test_angle() {
        val a = Vector2(0.5, Math.sqrt(3.0) / 2.0)
        Assert.assertEquals(a.angle, Math.toRadians(60.0), 1E-5)
    }

    @Test
    fun test_normalized() {
        val a = Vector2(3, 4).normalized
        Assert.assertEquals(a.x, 3.0 / 5.0, 1E-5)
        Assert.assertEquals(a.y, 4.0 / 5.0, 1E-5)
    }

    @Test
    fun test_zero() {
        val z = Vector2.Zero
        Assert.assertEquals(z.x, 0.0, 1E-5)
        Assert.assertEquals(z.y, 0.0, 1E-5)
    }

    @Test
    fun test_add() {
        val a = Vector2(2, 2)
        val b = Vector2(3, 3)
        Assert.assertEquals(a + b, Vector2(5, 5))
    }

    @Test
    fun test_subtract() {
        val a = Vector2(3, 3)
        val b = Vector2(2, 2)
        Assert.assertEquals(a - b, Vector2(1, 1))
    }

    @Test
    fun test_mult() {
        val a = Vector2(2, 2)
        val b = 5
        Assert.assertEquals(a * b, Vector2(10, 10))
    }

    @Test
    fun test_div() {
        val a = Vector2(10, 10)
        val b = 5
        Assert.assertEquals(a / b, Vector2(2, 2))
    }

    @Test
    fun test_unary_minus() {
        val a = Vector2(1, 1)
        Assert.assertEquals(-a, Vector2(-1, -1))
    }

    @Test
    fun test_divide_by_0() {
        val a = Vector2(10, 10)
        val b = 0.0
        try {
            val c = a / b
            assert(false)
        } catch (e: IllegalArgumentException) {
            println("Caught divide by 0 error")
        }
        assert(true)
    }

    @Test
    fun test_misc() {
        val a = Vector2(10, 10)
        val b = 10.0
        Assert.assertEquals(a.toString(), "X: $b, Y: $b")
        val c = Vector2(a)
        Assert.assertEquals(a, c)
    }
}
