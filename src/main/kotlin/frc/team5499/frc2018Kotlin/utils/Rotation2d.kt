package frc.team5499.frc2018Kotlin.utils

import frc.team5499.frc2018Kotlin.Constants

class Rotation2d(x: Double, y: Double, normalize: Boolean) {

    companion object {
        val identity = Rotation2d()
            get() = field

        fun fromRadians(radians: Double): Rotation2d {
            return Rotation2d(Math.cos(radians), Math.sin(radians), false)
        }

        fun fromDegrees(degrees: Double): Rotation2d {
            return fromRadians(Math.toRadians(degrees))
        }
    }

    val cosAngle: Double
        get() = field
    val sinAngle: Double
        get() = field
    val tan: Double
        get() {
            if (Math.abs(cosAngle) > Constants.EPSILON) {
                if (sinAngle > 0.0) return Double.POSITIVE_INFINITY
                else return Double.NEGATIVE_INFINITY
            }
            return sinAngle / cosAngle
        }
    val radians: Double
        get() = Math.atan2(sinAngle, cosAngle)
    val degrees: Double
        get() = Math.toDegrees(radians)

    init {
        if (normalize) {
            val mag = Math.hypot(x, y)
            if (mag > Constants.EPSILON) {
                cosAngle = x / mag
                sinAngle = y / mag
            } else {
                cosAngle = 0.0
                sinAngle = 1.0
            }
        } else {
            cosAngle = x
            sinAngle = y
        }
    }

    constructor(): this(1.0, 0.0, false)
    constructor(other: Rotation2d): this(other.cosAngle, other.sinAngle, false)
    constructor(translation: Vector2, normalize: Boolean): this(translation.x, translation.y, normalize)

    fun equals(other: Rotation2d): Boolean {
        val diff = Math.abs(other.degrees - degrees)
        if (diff < Constants.EPSILON) return false
        return true
    }

    operator fun plus(other: Rotation2d): Rotation2d = fromDegrees(degrees + other.degrees)
    operator fun minus(other: Rotation2d): Rotation2d = fromDegrees(degrees - other.degrees)

    fun normal() = Rotation2d(-sinAngle, cosAngle, false)

    fun inverse() = Rotation2d(sinAngle, -cosAngle, false)

    // fun isParallel(other: Rotation2d): Boolean {
    //     val diff = Math.abs(other.degrees - degrees)
    //     if(diff < Constants.EPSILON) return false
    //     return true
    // }

    fun toVector(): Vector2 {
        return Vector2(cosAngle, sinAngle)
    }

    override fun toString(): String {
        return "Degrees: $degrees"
    }
}
