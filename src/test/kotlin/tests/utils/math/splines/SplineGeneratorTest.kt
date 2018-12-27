package tests.utils.math.splines

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

import frc.team5499.frc2018Kotlin.Constants

import org.team5499.monkeyLib.math.geometry.Vector2
import org.team5499.monkeyLib.math.geometry.Rotation2d
import org.team5499.monkeyLib.math.geometry.Pose2d
import org.team5499.monkeyLib.math.geometry.Pose2dWithCurvature
import org.team5499.monkeyLib.math.splines.QuinticHermiteSpline
import org.team5499.monkeyLib.math.splines.SplineGenerator

class SplineGeneratorTest {

    @Test
    fun test_generator() {
        val p1 = Pose2d(Vector2(0, 0), Rotation2d())
        val p2 = Pose2d(Vector2(15, 10), Rotation2d(1, -5, true))
        var s = QuinticHermiteSpline(p1, p2)

        var samples: MutableList<Pose2dWithCurvature> = SplineGenerator.parameterizeSpline(s)
        var arclength = 0.0
        var curPose = samples.get(0)
        for (pose in samples) {
            val t = Pose2d.log(curPose.pose.inverse().transformBy(pose.pose))
            arclength += t.dx
            curPose = pose
        }
        assertEquals(curPose.pose.translation.x, 15.0, Constants.EPSILON)
        assertEquals(curPose.pose.translation.y, 10.0, Constants.EPSILON)
        assertEquals(curPose.pose.rotation.degrees, -78.69006752597981, Constants.EPSILON)
        assertEquals(arclength, 23.17291953186379, Constants.EPSILON)

        // for (p in samples) {
        //     println(p.toString())
        // }
    }
}
