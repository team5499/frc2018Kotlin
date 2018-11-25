package frc.team5499.frc2018Kotlin.utils.math

@SuppressWarnings("MagicNumber")
class CubicHermiteSpline(p0: Vector2, h0: Rotation2d, p1: Vector2, h1: Rotation2d) : Spline() {

    private val xCoeffs: Array<Double> // a, b, c, d
    private val yCoeffs: Array<Double> // a, b, c, d

    init {
        xCoeffs = arrayOf(0.0, 0.0, 0.0, 0.0)
        yCoeffs = arrayOf(0.0, 0.0, 0.0, 0.0)
        val scale = 2.0 * p0.distanceTo(p1)

        val x0 = p0.x
        val x1 = p1.x
        val dx0 = h0.cosAngle * scale
        val dx1 = h1.cosAngle * scale

        val y0 = p0.y
        val y1 = p1.y
        val dy0 = h0.sinAngle * scale
        val dy1 = h1.sinAngle * scale

        xCoeffs[0] = dx0 + dx1 + 2.0 * x0 - 2.0 * x1 // a
        xCoeffs[1] = -2.0 * dx0 - dx1 - 3.0 * x0 + 3.0 * x1 // b
        xCoeffs[2] = dx0 // c
        xCoeffs[3] = x0 // d

        yCoeffs[0] = dy0 + dy1 + 2.0 * y0 - 2.0 * y1
        yCoeffs[1] = -2.0 * dy0 - dy1 - 3.0 * y0 + 3.0 * y1
        yCoeffs[2] = dy0
        yCoeffs[3] = y0
    }

    override fun getPoint(t: Double): Vector2 {
        val x = t * t * t * xCoeffs[0] + t * t * xCoeffs[1] + t * xCoeffs[2] + xCoeffs[3]
        val y = t * t * t * yCoeffs[0] + t * t * yCoeffs[1] + t * yCoeffs[2] + yCoeffs[3]
        return Vector2(x, y)
    }

    override fun getHeading(t: Double): Rotation2d {
        val dx = 3.0 * t * t * xCoeffs[0] + 2.0 * t * xCoeffs[1] + xCoeffs[2]
        val dy = 3.0 * t * t * yCoeffs[0] + 2.0 * t * yCoeffs[1] + xCoeffs[2]
        return Rotation2d(dy, dx, true)
    }

    override fun getCurvature(t: Double): Double {
        val dx = 3.0 * t * t * xCoeffs[0] + 2.0 * t * xCoeffs[1] + xCoeffs[2]
        val dy = 3.0 * t * t * xCoeffs[0] + 2.0 * t * xCoeffs[1] + xCoeffs[2]
        val ddx = 6 * t * xCoeffs[0] + 2 * xCoeffs[1]
        val ddy = 6 * t * xCoeffs[1] + 2 * xCoeffs[1]
        return (dx * ddy - dy * ddx) / ((dx * dx + dy * dy) * Math.sqrt(dx * dx + dy * dy))
    }
}
