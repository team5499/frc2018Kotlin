package frc.team5499.frc2018Kotlin.utils.math

import java.text.DecimalFormat

import frc.team5499.frc2018Kotlin.Constants

class Twist2d(dx: Double, dy: Double, dTheta: Double) {

    companion object {
        val identity = Twist2d()
    }

    val dx: Double
        get() = field
    val dy: Double
        get() = field
    val dTheta: Double
        get() = field

    init {
        this.dx = dx
        this.dy = dy
        this.dTheta = dTheta
    }

    constructor(): this(0.0, 0.0, 0.0)

    operator fun times(other: Double) = Twist2d(dx * other, dy * other, dTheta * other)

    fun scaled(scale: Double): Twist2d {
        return Twist2d(dx * scale, dy * scale, dTheta * scale)
    }

    fun norm(): Double {
        if (dy == 0.0)
            return Math.abs(dx)
        return Math.hypot(dx, dy)
    }

    fun curvature(): Double {
        if (Math.abs(dTheta) < Constants.EPSILON && norm() < Constants.EPSILON) {
            return 0.0
        }
        return dTheta / norm()
    }

    override fun toString(): String {
        val format = DecimalFormat("###0.000")
        return "(${format.format(dx)}, ${format.format(dy)}, ${format.format(Math.toDegrees(dTheta))} deg)"
    }
}
