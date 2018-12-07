package frc.team5499.frc2018Kotlin.utils

interface Interpolable<T> {
    fun interpolate(other: T, x: Double): T
}
