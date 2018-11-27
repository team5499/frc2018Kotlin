package tests.utils.math

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

import frc.team5499.frc2018Kotlin.utils.math.Vector2
import frc.team5499.frc2018Kotlin.utils.math.Rotation2d
import frc.team5499.frc2018Kotlin.utils.math.Pose2d
import frc.team5499.frc2018Kotlin.utils.math.Twist2d

import frc.team5499.frc2018Kotlin.Constants

public class GeometryTests {

    companion object {
        private const val testEpsilon = Constants.EPSILON
    }

    @Suppress("LongMethod")
    @Test
    fun testVector2() {
        var pos1 = Vector2()
        assertEquals(0.0, pos1.x, testEpsilon)
        assertEquals(0.0, pos1.y, testEpsilon)
        assertEquals(0.0, pos1.magnitude, testEpsilon)

        pos1 = Vector2(3.0, 4.0)
        assertEquals(3.0, pos1.x, testEpsilon)
        assertEquals(4.0, pos1.y, testEpsilon)
        assertEquals(5.0, pos1.magnitude, testEpsilon)

        pos1 = Vector2(3.152, 4.1666)
        var pos2 = -pos1
        assertEquals(-pos2.x, pos1.x, testEpsilon)
        assertEquals(-pos2.y, pos1.y, testEpsilon)
        assertEquals(pos1.magnitude, pos2.magnitude, testEpsilon)

        pos1 = Vector2(2, 0)
        var rot1 = Rotation2d.fromDegrees(90.0)
        pos2 = pos1.rotateBy(rot1)
        assertEquals(0.0, pos2.x, testEpsilon)
        assertEquals(2.0, pos2.y, testEpsilon)
        assertEquals(pos1.magnitude, pos2.magnitude, testEpsilon)

        pos1 = Vector2(2, 0)
        rot1 = Rotation2d.fromDegrees(-45.0)
        pos2 = pos1.rotateBy(rot1)
        assertEquals(Math.sqrt(2.0), pos2.x, testEpsilon)
        assertEquals(-Math.sqrt(2.0), pos2.y, testEpsilon)

        pos1 = Vector2(2, 0)
        pos2 = Vector2(-2, 1)
        var pos3 = pos1.translateBy(pos2)
        assertEquals(0.0, pos3.x, testEpsilon)
        assertEquals(1.0, pos3.y, testEpsilon)
        assertEquals(1.0, pos3.magnitude, testEpsilon)

        val identity = Vector2()
        pos1 = Vector2(2.16612, -23.55)
        pos2 = pos1.translateBy(-pos1)
        assertEquals(identity.x, pos2.x, testEpsilon)
        assertEquals(identity.y, pos2.y, testEpsilon)
        assertEquals(identity.magnitude, pos2.magnitude, testEpsilon)

        // test interpolation eventually
    }

    @Suppress("LongMethod")
    @Test
    fun testTwist() {
        var twist = Twist2d(1.0, 0.0, 0.0)
        var pose = Pose2d.exp(twist)
        assertEquals(1.0, pose.translation.x, testEpsilon)
        assertEquals(0.0, pose.translation.y, testEpsilon)
        assertEquals(0.0, pose.rotation.degrees, testEpsilon)

        twist = Twist2d(1.0, 0.0, 0.0)
        pose = Pose2d.exp(twist * 2.5)
        assertEquals(2.5, pose.translation.x, testEpsilon)
        assertEquals(0.0, pose.translation.y, testEpsilon)
        assertEquals(0.0, pose.rotation.degrees, testEpsilon)

        pose = Pose2d(Vector2(2.0, 2.0), Rotation2d.fromRadians(Math.PI / 2))
        twist = Pose2d.log(pose)
        assertEquals(Math.PI, twist.dx, testEpsilon)
        assertEquals(0.0, twist.dy, testEpsilon)
        assertEquals(Math.PI / 2, twist.dTheta, testEpsilon)

        val new_pose = Pose2d.exp(twist)
        assertEquals(new_pose.translation.x, pose.translation.x, testEpsilon)
        println("new pose y: ${new_pose.translation.y}, pose y: ${pose.translation.y}")
        assertEquals(new_pose.translation.y, pose.translation.y, testEpsilon)
        assertEquals(new_pose.rotation.degrees, pose.rotation.degrees, testEpsilon)
    }

    @Suppress("LongMethod")
    @Test
    fun testRotation() {
        // Test constructors
        var rot1 = Rotation2d()
        assertEquals(1.0, rot1.cosAngle, testEpsilon)
        assertEquals(0.0, rot1.sinAngle, testEpsilon)
        println(rot1.tan)
        assertEquals(0.0, rot1.tan, testEpsilon)
        assertEquals(0.0, rot1.degrees, testEpsilon)
        assertEquals(0.0, rot1.radians, testEpsilon)

        rot1 = Rotation2d(1.0, 1.0, true)
        assertEquals(Math.sqrt(2.0) / 2.0, rot1.cosAngle, testEpsilon)
        assertEquals(Math.sqrt(2.0) / 2.0, rot1.sinAngle, testEpsilon)
        assertEquals(1.0, rot1.tan, testEpsilon)
        assertEquals(45.0, rot1.degrees, testEpsilon)
        assertEquals(Math.PI / 4.0, rot1.radians, testEpsilon)

        rot1 = Rotation2d.fromRadians(Math.PI / 2)
        assertEquals(0.0, rot1.cosAngle, testEpsilon)
        assertEquals(1.0, rot1.sinAngle, testEpsilon)
        assertTrue(1.0 / testEpsilon < rot1.tan)
        assertEquals(90.0, rot1.degrees, testEpsilon)
        assertEquals(Math.PI / 2.0, rot1.radians, testEpsilon)

        rot1 = Rotation2d.fromDegrees(270.0)
        assertEquals(0.0, rot1.cosAngle, testEpsilon)
        assertEquals(-1.0, rot1.sinAngle, testEpsilon)
        println(rot1.tan)
        assertTrue(-1.0 / testEpsilon > rot1.tan)
        assertEquals(-90.0, rot1.degrees, testEpsilon)
        assertEquals(-Math.PI / 2.0, rot1.radians, testEpsilon)

        // Test inversion
        rot1 = Rotation2d.fromDegrees(270.0)
        var rot2 = rot1.inverse()
        println(rot2.cosAngle)
        assertEquals(0.0, rot2.cosAngle, testEpsilon)
        assertEquals(1.0, rot2.sinAngle, testEpsilon)
        assertTrue(1.0 / testEpsilon < rot2.tan)
        assertEquals(90.0, rot2.degrees, testEpsilon)
        assertEquals(Math.PI / 2, rot2.radians, testEpsilon)

        rot1 = Rotation2d.fromDegrees(1.0)
        rot2 = rot1.inverse()
        assertEquals(rot1.cosAngle, rot2.cosAngle, testEpsilon)
        assertEquals(-rot1.sinAngle, rot2.sinAngle, testEpsilon)
        assertEquals(-1.0, rot2.degrees, testEpsilon)

        // Test rotateBy
        rot1 = Rotation2d.fromDegrees(45.0)
        rot2 = Rotation2d.fromDegrees(45.0)
        var rot3 = rot1.rotateBy(rot2)
        assertEquals(0.0, rot3.cosAngle, testEpsilon)
        assertEquals(1.0, rot3.sinAngle, testEpsilon)
        assertTrue(1.0 / testEpsilon < rot3.tan)
        assertEquals(90.0, rot3.degrees, testEpsilon)
        assertEquals(Math.PI / 2.0, rot3.radians, testEpsilon)

        rot1 = Rotation2d.fromDegrees(45.0)
        rot2 = Rotation2d.fromDegrees(-45.0)
        rot3 = rot1.rotateBy(rot2)
        assertEquals(1.0, rot3.cosAngle, testEpsilon)
        assertEquals(0.0, rot3.sinAngle, testEpsilon)
        assertEquals(0.0, rot3.tan, testEpsilon)
        assertEquals(0.0, rot3.degrees, testEpsilon)
        assertEquals(0.0, rot3.radians, testEpsilon)

        // A rotation times its inverse should be the identity
        val identity = Rotation2d()
        rot1 = Rotation2d.fromDegrees(21.45)
        rot2 = rot1.rotateBy(rot1.inverse())
        assertEquals(identity.cosAngle, rot2.cosAngle, testEpsilon)
        assertEquals(identity.sinAngle, rot2.sinAngle, testEpsilon)
        assertEquals(identity.degrees, rot2.degrees, testEpsilon)

        // Test interpolation

        // rot1 = Rotation2d.fromDegrees(45)
        // rot2 = Rotation2d.fromDegrees(135)
        // rot3 = rot1.interpolate(rot2, .5)
        // assertEquals(90, rot3.getDegrees(), testEpsilon)

        // rot1 = Rotation2d.fromDegrees(45)
        // rot2 = Rotation2d.fromDegrees(135)
        // rot3 = rot1.interpolate(rot2, .75)
        // assertEquals(112.5, rot3.getDegrees(), testEpsilon)

        // rot1 = Rotation2d.fromDegrees(45)
        // rot2 = Rotation2d.fromDegrees(-45)
        // rot3 = rot1.interpolate(rot2, .5)
        // assertEquals(0, rot3.getDegrees(), testEpsilon)

        // rot1 = Rotation2d.fromDegrees(45)
        // rot2 = Rotation2d.fromDegrees(45)
        // rot3 = rot1.interpolate(rot2, .5)
        // assertEquals(45, rot3.getDegrees(), testEpsilon)

        // rot1 = Rotation2d.fromDegrees(45)
        // rot2 = Rotation2d.fromDegrees(45)
        // rot3 = rot1.interpolate(rot2, .5)
        // assertEquals(45, rot3.getDegrees(), testEpsilon)

        // Test parallel.
        // rot1 = Rotation2d.fromDegrees(45)
        // rot2 = Rotation2d.fromDegrees(45)
        // assertTrue(rot1.isParallel(rot2))

        // rot1 = Rotation2d.fromDegrees(45)
        // rot2 = Rotation2d.fromDegrees(-45)
        // assertFalse(rot1.isParallel(rot2))

        // rot1 = Rotation2d.fromDegrees(45)
        // rot2 = Rotation2d.fromDegrees(-135)
        // assertTrue(rot1.isParallel(rot2))
    }
}
