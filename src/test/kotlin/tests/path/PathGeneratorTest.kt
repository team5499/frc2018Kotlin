package tests.path

import org.junit.jupiter.api.Test

import frc.team5499.frc2018Kotlin.Paths

public class PathGeneratorTest {

    @Test
    fun test_generator() {
        assert(Paths.startLeftSwitch.reversed == true)
        // println(PathGenerator.Paths.startLeftSwitchPath.toCSV())
    }
}
