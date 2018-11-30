package tests.utils.path

import org.junit.jupiter.api.Test

import frc.team5499.frc2018Kotlin.path.PathGenerator

public class PathGeneratorTest {

    @Test
    fun test_generator() {
        PathGenerator.generatePathSet()
        assert(PathGenerator.pathSet!!.startLeftSwitchPath.reversed == true)
        // assert(PathGenerator.pathSet!!.startLeftSwitchPath.startPose)
        println(PathGenerator.pathSet!!.startLeftSwitchPath.toCSV())
    }
}
