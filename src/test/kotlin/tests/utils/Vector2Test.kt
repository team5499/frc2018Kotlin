package tests.utils

import org.junit.Test

import frc.team5499.frc2018Kotlin.utils.Vector2

public class Vector2Test {
    @Test
    fun test_dot() {
        val a = Vector2(3, 4)
        val b = Vector2(5, 6)
        assert(a.dot(b) == 39.0)
    }
}
