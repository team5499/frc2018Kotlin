package frc.team5499.frc2018Kotlin.utils

data class Vector2(val x: Double, val y: Double) {

    constructor(other: Vector2): this(other.x, other.y)

    constructor(x: Int, y: Int): this(x.toDouble(), y.toDouble())
    val magnitude by lazy { Math.hypot(y, x) }
    val angle by lazy { Math.atan2(y, x) }
    val normalized by lazy {
        val len = magnitude
        Vector2(x / len.toDouble(), y / len.toDouble())
    }

    operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)

    operator fun minus(other: Vector2) = Vector2(x - other.x, y - other.y)

    operator fun times(coef: Double) = Vector2(x * coef, y * coef)

    operator fun div(coef: Double) = when (coef) {
        0.0 -> throw IllegalArgumentException("Division by 0")
        else -> Vector2(x / coef, y / coef)
    }

    operator fun unaryMinus() = Vector2(-x, -y)

    fun dot(other: Vector2) = x * other.x + y * other.y

    fun distanceTo(other: Vector2) = (this - other).magnitude

    fun getHeading(): Double {
        var rawHeading: Double = 450.0 - Math.toDegrees(Math.atan2(y, x))
        while (rawHeading < 0.0) {
            rawHeading += 360.0
        }
        while (rawHeading > 360.0) {
            rawHeading -= 360.0
        }
        return rawHeading
    }

    override fun toString(): String = "X: $x, Y: $y"

    companion object {
        val Zero = Vector2(0, 0)

        fun distanceBetween(a: Vector2, b: Vector2) = (a - b).magnitude

        fun unitDirectionVector(vector: Vector2): Vector2 = vector * 1.0 / vector.magnitude

        fun copyVector(original: Vector2): Vector2 = Vector2(original.x, original.y)

        fun representHeadingWithUnitVector(heading: Double): Vector2 = Vector2(Math.cos(Math.toRadians(450 - heading)), Math.sin(Math.toRadians(450 - heading)))

        fun angleBetween(from: Vector2, to: Vector2): Double {
        var rawAngleDelta: Double = from.getHeading() - to.getHeading()
        var adjustedAngleDelta: Double = -360.0 - rawAngleDelta
        while (adjustedAngleDelta > 180.0) {
            adjustedAngleDelta -= 360.0
        }
        while (adjustedAngleDelta < -180.0) {
            adjustedAngleDelta += 360.0
        }
        return adjustedAngleDelta
    }
    }
}
