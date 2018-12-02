package tests.utils.path

import org.junit.jupiter.api.Test

import frc.team5499.frc2018Kotlin.path.PathGenerator

public class PathGeneratorTest {

    @Test
    fun test_generator() {
        assert(PathGenerator.Paths.startLeftSwitchPath.reversed == true)
        println(PathGenerator.Paths.startLeftSwitchPath.toCSV())
    }
}
