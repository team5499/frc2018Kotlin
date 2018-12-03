package frc.team5499.frc2018Kotlin.utils.math.geometry

import frc.team5499.frc2018Kotlin.Constants

@Suppress("TooManyFunctions")
class Pose2d(translation: Vector2, rotation: Rotation2d) : Geometric<Pose2d> {

    companion object {

        @Suppress("MagicNumber")
        fun log(transform: Pose2d): Twist2d {
            val dTheta = transform.rotation.radians
            val halfTheta = 0.5 * dTheta
            val cosMinusOne = transform.rotation.cosAngle - 1.0
            val halfThetaByTanOfHalfDtheta: Double
            if (Math.abs(cosMinusOne) < Constants.EPSILON) {
                halfThetaByTanOfHalfDtheta = 1.0 - ((1.0 / 12.0) * (dTheta * dTheta))
            } else {
                halfThetaByTanOfHalfDtheta = -(halfTheta * transform.rotation.sinAngle) / cosMinusOne
            }
            val translationPart = transform.translation.rotateBy(
                Rotation2d(halfThetaByTanOfHalfDtheta, -halfTheta, false)
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

        fun fromRotation(rotation: Rotation2d) = Pose2d(Vector2(), rotation)
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
    constructor(other: Pose2d): this(other.translation, other.rotation)

    fun inverse(): Pose2d {
        val rotationInverted = rotation.inverse()
        return Pose2d((-translation).rotateBy(rotationInverted), rotationInverted)
    }

    fun normal(): Pose2d {
        return Pose2d(translation, rotation.normal())
    }

    fun isColinear(other: Pose2d): Boolean {
        if (other.rotation.isParallel(rotation)) return false
        val twist = log(inverse().transformBy(other))
        return Math.abs(twist.dx) < Constants.EPSILON && Math.abs(twist.dTheta) < Constants.EPSILON
    }

    @Suppress("ReturnCount")
    override fun interpolate(other: Pose2d, x: Double): Pose2d {
        if (x <= 0) {
            return Pose2d(this)
        } else if (x >= 1) {
            return Pose2d(other)
        }
        val twist = Pose2d.log(inverse().transformBy(other))
        return transformBy(Pose2d.exp(twist.scaled(x)))
    }

    fun transformBy(other: Pose2d): Pose2d {
        return Pose2d(translation.translateBy(other.translation.rotateBy(rotation)), rotation.rotateBy(other.rotation))
    }

    fun transformBy(other: Vector2): Pose2d {
        return Pose2d(translation.translateBy(other), rotation)
    }

    fun mirror(): Pose2d {
        return Pose2d(Vector2(translation.x, -translation.y), rotation.inverse())
    }

    override fun toString(): String {
        return "Translation: $translation, Rotation: $rotation"
    }

    override fun toCSV() = "${translation.toCSV()},${rotation.toCSV()}"

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Pose2d) return false
        return (other.translation.equals(translation) && other.rotation.equals(rotation))
    }

    override fun hashCode() = super.hashCode()
}
