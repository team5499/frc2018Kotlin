package frc.team5499.frc2018Kotlin.utils.math.splines

import frc.team5499.frc2018Kotlin.utils.math.Vector2
import frc.team5499.frc2018Kotlin.utils.math.Rotation2d
import frc.team5499.frc2018Kotlin.utils.math.Pose2d

@SuppressWarnings("MagicNumber", "TooManyFunctions")
class QuinticHermiteSpline(p0: Vector2, h0: Rotation2d, p1: Vector2, h1: Rotation2d) : Spline() {

    private val ax: Double
    private val bx: Double
    private val cx: Double
    private val dx: Double
    private val ex: Double
    private val fx: Double

    private val ay: Double
    private val by: Double
    private val cy: Double
    private val dy: Double
    private val ey: Double
    private val fy: Double

    private val x0: Double
    private val x1: Double
    private val dx0: Double
    private val dx1: Double
    private val ddx0: Double
    private val ddx1: Double

    private val y0: Double
    private val y1: Double
    private val dy0: Double
    private val dy1: Double
    private val ddy0: Double
    private val ddy1: Double

    init {
        val scale = 1.2 * p0.distanceTo(p1)

        // x vars
        x0 = p0.x
        x1 = p1.x
        dx0 = h0.cosAngle * scale
        dx1 = h1.cosAngle * scale
        ddx0 = 0.0
        ddx1 = 0.0

        // y vars
        y0 = p0.y
        y1 = p1.y
        dy0 = h0.sinAngle * scale
        dy1 = h1.sinAngle * scale
        ddy0 = 0.0
        ddy1 = 0.0

        ax = -6 * x0 - 3 * dx0 - 0.5 * ddx0 + 0.5 * ddx1 - 3 * dx1 + 6 * x1
        bx = 15 * x0 + 8 * dx0 + 1.5 * ddx0 - ddx1 + 7 * dx1 - 15 * x1
        cx = -10 * x0 - 6 * dx0 - 1.5 * ddx0 + 0.5 * ddx1 - 4 * dx1 + 10 * x1
        dx = 0.5 * ddx0
        ex = dx0
        fx = x0

        ay = -6 * y0 - 3 * dy0 - 0.5 * ddy0 + 0.5 * ddy1 - 3 * dy1 + 6 * y1
        by = 15 * y0 + 8 * dy0 + 1.5 * ddy0 - ddy1 + 7 * dy1 - 15 * y1
        cy = -10 * y0 - 6 * dy0 - 1.5 * ddy0 + 0.5 * ddy1 - 4 * dy1 + 10 * y1
        dy = 0.5 * ddy0
        ey = dy0
        fy = y0
    }

    constructor(p0: Pose2d, p1: Pose2d): this(p0.translation, p0.rotation, p1.translation, p1.rotation)

    fun getStartPose() = Pose2d(Vector2(x0, y0), Rotation2d(dx0, dy0, true))
    fun getEndPose() = Pose2d(Vector2(x1, y1), Rotation2d(dx1, dy1, true))

    override fun getPoint(t: Double): Vector2 {
        val x = ax * t * t * t * t * t + bx * t * t * t * t + cx * t * t * t + dx * t * t + ex * t + fx
        val y = ay * t * t * t * t * t + by * t * t * t * t + cy * t * t * t + dy * t * t + ey * t + fy
        return Vector2(x, y)
    }

    private fun dx(t: Double) = 5 * ax * t * t * t * t + 4 * bx * t * t * t + 3 * cx * t * t + 2 * dx * t + ex
    private fun dy(t: Double) = 5 * ay * t * t * t * t + 4 * by * t * t * t + 3 * cy * t * t + 2 * dy * t + ey

    private fun ddx(t: Double) = 20 * ax * t * t * t + 12 * bx * t * t + 6 * cx * t + 2 * dx
    private fun ddy(t: Double) = 20 * ay * t * t * t + 12 * by * t * t + 6 * cy * t + 2 * dy

    private fun dddx(t: Double) = 60 * ax * t * t + 24 * bx * t + 6 * cx
    private fun dddy(t: Double) = 60 * ay * t * t + 24 * by * t + 6 * cy

    override fun getVelocity(t: Double) = Math.hypot(dx(t), dy(t))

    override fun getCurvature(t: Double): Double {
        var temp = dx(t) * ddy(t) - ddx(t) * dy(t)
        temp /= ((dx(t) * dx(t) + dy(t) * dy(t)) * Math.sqrt((dx(t) * dx(t) + dy(t) * dy(t))))
        return temp
    }

    override fun getDCurvature(t: Double): Double {
        val dx2dy2 = (dx(t) * dx(t) + dy(t) * dy(t))
        val num = (dx(t) * dddy(t) - dddx(t) * dy(t)) * dx2dy2 -
            3 * (dx(t) * ddy(t) - ddx(t) * dy(t)) * (dx(t) * ddx(t) + dy(t) * ddy(t))
        return num / (dx2dy2 * dx2dy2 * Math.sqrt(dx2dy2))
    }

    fun dCurvature2(t: Double): Double {
        val dx2dy2 = (dx(t) * dx(t) + dy(t) * dy(t))
        val num = (dx(t) * dddy(t) - dddx(t) * dy(t)) * dx2dy2 -
            3 * (dx(t) * ddy(t) - ddx(t) * dy(t)) * (dx(t) * ddx(t) + dy(t) * ddy(t))
        return num * num / (dx2dy2 * dx2dy2 * dx2dy2 * dx2dy2 * dx2dy2)
    }

    override fun getHeading(t: Double) = Rotation2d(dx(t), dy(t), true)

    private fun sumDCurvature2(): Double {
        val dt = 1.0 / kSamples
        var sum = 0.0
        var t = 0.0
        while (t < 1.0) {
            sum += (dt * dCurvature2(t))
            t += dt
        }
        return sum
    }

    companion object {
        private const val kStepSize = 1.0
        private const val kMinDelta = 0.001
        private const val kSamples = 100
        private const val kMaxIterations = 100
    }
}
