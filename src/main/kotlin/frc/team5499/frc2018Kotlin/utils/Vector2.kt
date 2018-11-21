package frc.team5499.frc2018Kotlin.utils

data class Vector2(val x: Double, val y: Double) {

    constructor(other: Vector2): this(other.x, other.y)
    constructor(): this(0.0, 0.0)
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

    fun translateBy(x: Double, y: Double): Vector2 = Vector2(this.x + x, this.y + y)

    override fun toString(): String = "X: $x, Y: $y"

    companion object {
        val Zero = Vector2(0, 0)
        fun distanceBetween(a: Vector2, b: Vector2) = (a - b).magnitude
    }
}
