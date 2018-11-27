package frc.team5499.frc2018Kotlin.utils.math

import frc.team5499.frc2018Kotlin.Constants

open class Pose2d(translation: Vector2, rotation: Rotation2d) {

    companion object {

        @Suppress("MagicNumber")
        fun log(transform: Pose2d): Twist2d {
            val dTheta = transform.rotation.radians
            val halfTheta = 0.5 * dTheta
            val cosMinusOne = transform.rotation.cosAngle - 1.0
            val halftheta_by_tan_of_halfdtheta: Double
            if (Math.abs(cosMinusOne) < Constants.EPSILON) {
                halftheta_by_tan_of_halfdtheta = 1.0 - 1.0 / 12.0 * dTheta * dTheta
            } else {
                halftheta_by_tan_of_halfdtheta = -(halfTheta * transform.rotation.sinAngle) / cosMinusOne
            }
            val translationPart = transform.translation.rotateBy(
                Rotation2d(halftheta_by_tan_of_halfdtheta, -halfTheta, false)
            )
            return Twist2d(translationPart.x, translationPart.y, dTheta)
        }

        @Suppress("MagicNumber")
        fun exp(delta: Twist2d): Pose2d {
            val sinTheta = Math.sin(delta.dTheta)
            val cosTheta = Math.cos(delta.dTheta)
            val s: Double
            val c: Double
            if (Math.abs(delta.dTheta) < Constants.EPSILON) {
                s = 1.0 - 1.0 / 6.0 * delta.dTheta * delta.dTheta
                c = .5 * delta.dTheta
            } else {
                s = sinTheta / delta.dTheta
                c = (1.0 - cosTheta) / delta.dTheta
            }
            return Pose2d(Vector2(delta.dx * s - delta.dy * c, delta.dx * c + delta.dy * s),
                Rotation2d(cosTheta, sinTheta, false))
        }
    }

    val translation: Vector2
        get() = field
    val rotation: Rotation2d
        get() = field

    init {
        this.translation = translation
        this.rotation = rotation
    }

    constructor(): this(Vector2(), Rotation2d())

    fun inverse(): Pose2d {
        val rotationInverted = rotation.inverse()
        return Pose2d((-translation).rotateBy(rotationInverted), rotationInverted)
    }

    fun transformBy(other: Pose2d): Pose2d {
        return Pose2d(translation.translateBy(other.translation.rotateBy(rotation)), rotation.rotateBy(other.rotation))
    }

    override fun toString(): String {
        return "Translation: $translation, Rotation: $rotation"
    }
}
