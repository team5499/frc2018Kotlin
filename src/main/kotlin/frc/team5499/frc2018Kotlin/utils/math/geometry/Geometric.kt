package frc.team5499.frc2018Kotlin.utils.math.geometry

import frc.team5499.frc2018Kotlin.utils.Interpolable
import frc.team5499.frc2018Kotlin.utils.CSVWritable

interface Geometric<T> : Interpolable<T>, CSVWritable {

    public override fun toString(): String

    public override fun equals(other: Any?): Boolean

    public override fun toCSV(): String

    public override fun interpolate(other: T, x: Double): T

    public override fun hashCode(): Int
}
