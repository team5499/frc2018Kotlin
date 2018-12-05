package frc.team5499.frc2018Kotlin.utils.math.geometry

@Suppress("TooManyFunctions")
class Vector2(val x: Double, val y: Double) : Geometric<Vector2> {

    companion object {
        fun distanceBetween(a: Vector2, b: Vector2) = (a - b).magnitude
        fun cross(a: Vector2, b: Vector2) = a.x * b.y - a.y * b.x
    }

    val magnitude by lazy { Math.hypot(y, x) }
    val angle by lazy { Math.atan2(y, x) }
    val normalized by lazy {
        val len = magnitude
        Vector2(x / len.toDouble(), y / len.toDouble())
    }

    constructor(other: Vector2): this(other.x, other.y)
    constructor(): this(0.0, 0.0)
    constructor(x: Int, y: Int): this(x.toDouble(), y.toDouble())
    constructor(start: Vector2, end: Vector2): this(end.x - start.x, end.y - start.y)

    operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)

    operator fun minus(other: Vector2) = Vector2(x - other.x, y - other.y)

    operator fun times(coef: Int) = Vector2(this * coef.toDouble())

    operator fun times(coef: Double) = Vector2(x * coef, y * coef)

    operator fun div(coef: Double) = when (coef) {
        0.0 -> throw IllegalArgumentException("Division by 0")
        else -> Vector2(x / coef, y / coef)
    }

    operator fun unaryMinus() = Vector2(-x, -y)

    fun dot(other: Vector2) = x * other.x + y * other.y

    fun distanceTo(other: Vector2) = (this - other).magnitude

    fun translateBy(other: Vector2) = Vector2(this + other)
    fun translateBy(x: Double, y: Double) = Vector2(this.x + x, this.y + y)

    fun rotateBy(r: Rotation2d) = Vector2(x * r.cosAngle - y * r.sinAngle, x * r.sinAngle + y * r.cosAngle)

    fun extrapolate(other: Vector2, x: Double) = Vector2(
        x * (other.x - this.x) + this.x, x * (other.y - this.y) + this.y
    )

    @Suppress("ReturnCount")
    override fun interpolate(other: Vector2, x: Double): Vector2 {
        if (x <= 0) return Vector2(this)
        else if (x >= 1) return Vector2(other)
        else return extrapolate(other, x)
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Vector2) return false
        return x == other.x && y == other.y
    }

    override fun hashCode() = super.hashCode()

    override fun toString(): String = "(X: %.3f, Y: %.3f)".format(x, y)

    override fun toCSV() = "$x,$y"
}
