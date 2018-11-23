package tests.utils.path

import org.junit.jupiter.api.Test
// import org.junit.Assert

import frc.team5499.frc2018Kotlin.path.PathGenerator
import frc.team5499.frc2018Kotlin.utils.Vector2

public class PathGeneratorTest {

    @Test
    fun test_generator_lengths() {
        val waypoints: MutableList<Vector2> = mutableListOf(Vector2(0.0, 0.0), Vector2(20.0, 0.0), Vector2(20.0, 20.0))
        val pair = PathGenerator.generateSmoothPath(waypoints)
        println("Path length: ${pair.first.size}, Velo Length: ${pair.second.size}")
        println("Path: ${pair.first}")
        println("Velo: ${pair.second}")
        assert(pair.first.size == pair.second.size)
    }
}
