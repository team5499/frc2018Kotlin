package frc.team5499.frc2018Kotlin.utils.math.splines

import frc.team5499.frc2018Kotlin.utils.math.geometry.Vector2
import frc.team5499.frc2018Kotlin.utils.math.geometry.Pose2d
import frc.team5499.frc2018Kotlin.utils.math.geometry.Rotation2d

@SuppressWarnings("MagicNumber")
class CubicHermiteSpline(p0: Vector2, h0: Rotation2d, p1: Vector2, h1: Rotation2d) : Spline() {

    // private val xCoeffs: Array<Double> // a, b, c, d
    // private val yCoeffs: Array<Double> // a, b, c, d

    private val ax: Double
    private val bx: Double
    private val cx: Double
    private val dx: Double

    private val ay: Double
    private val by: Double
    private val cy: Double
    private val dy: Double

    init {
        val scale = 2.0 * p0.distanceTo(p1)

        val x0 = p0.x
        val x1 = p1.x
        val dx0 = h0.cosAngle * scale
        val dx1 = h1.cosAngle * scale

        val y0 = p0.y
        val y1 = p1.y
        val dy0 = h0.sinAngle * scale
        val dy1 = h1.sinAngle * scale

        // ax = dx0 + dx1 + 2.0 * x0 - 2.0 * x1 // a
        // xCoeffs[1] = -2.0 * dx0 - dx1 - 3.0 * x0 + 3.0 * x1 // b
        // cx = dx0 // c
        // xCoeffs[3] = x0 // d

        // ay = dy0 + dy1 + 2.0 * y0 - 2.0 * y1 // a
        // by = -2.0 * dy0 - dy1 - 3.0 * y0 + 3.0 * y1 // b
        // cy = dy0 // c
        // yCoeffs[3] = y0 // d

        ax = dx0 + dx1 + 2.0 * x0 - 2.0 * x1
        bx = -2.0 * dx0 - dx1 - 3.0 * x0 + 3.0 * x1
        cx = dx0
        dx = x0

        ay = dy0 + dy1 + 2.0 * y0 - 2.0 * y1
        by = -2.0 * dy0 - dy1 - 3.0 * y0 + 3.0
        cy = dy0
        dy = y0
    }

    constructor(p0: Pose2d, p1: Pose2d): this(p0.translation, p0.rotation, p1.translation, p1.rotation)
    constructor(): this(Pose2d(), Pose2d())

    override fun getPoint(t: Double): Vector2 {
        val x = t * t * t * ax + t * t * bx + t * cx + dx
        val y = t * t * t * ay + t * t * by + t * cy + dy
        return Vector2(x, y)
    }

    override fun getHeading(t: Double): Rotation2d {
        val dx = 3.0 * t * t * ax + 2.0 * t * bx + cx
        val dy = 3.0 * t * t * ay + 2.0 * t * by + cx
        return Rotation2d(dy, dx, true)
    }

    override fun getCurvature(t: Double): Double {
        val dx = 3.0 * t * t * ax + 2.0 * t * bx + cx
        val dy = 3.0 * t * t * ay + 2.0 * t * by + cy
        val ddx = 6.0 * t * ax + 2.0 * bx
        val ddy = 6.0 * t * ay + 2.0 * by
        return (dx * ddy - dy * ddx) / ((dx * dx + dy * dy) * Math.sqrt(dx * dx + dy * dy))
    }

    override fun getDCurvature(t: Double) = 0.0

    override fun getVelocity(t: Double) = 0.0
}
