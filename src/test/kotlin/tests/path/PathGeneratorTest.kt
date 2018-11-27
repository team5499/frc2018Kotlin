package tests.utils.path

import org.junit.jupiter.api.Test
// import org.junit.Assert

import frc.team5499.frc2018Kotlin.utils.math.Vector2
import frc.team5499.frc2018Kotlin.path.PathGenerator

public class PathGeneratorTest {

    @Test
    fun test_generator_lengths() {
        val waypoints: MutableList<Vector2> = mutableListOf(Vector2(0.0, 0.0), Vector2(20.0, 0.0), Vector2(20.0, 20.0))
        val path = PathGenerator.generatePath(waypoints, false)
        // println(path.toString())
    }
}
