package frc.team5499.frc2018Kotlin.path

import frc.team5499.frc2018Kotlin.utils.CSVWritable

import frc.team5499.frc2018Kotlin.utils.math.geometry.Vector2
import frc.team5499.frc2018Kotlin.utils.math.geometry.Pose2dWithCurvature

class Path(points: MutableList<Pose2dWithCurvature>, reversed: Boolean = false) : CSVWritable {

    private val mPoints: MutableList<Pose2dWithCurvature>

    val pathLength: Int
        get() = mPoints.size

    val reversed: Boolean
        get() = field

    val startPose: Pose2dWithCurvature
        get() = Pose2dWithCurvature(mPoints.get(0))

    val endPose: Pose2dWithCurvature
        get() = Pose2dWithCurvature(mPoints.get(pathLength - 1))

    init {
        if (points.size < 2) throw IllegalArgumentException("Needs to be more than 2 points for a path")
        this.reversed = reversed
        mPoints = mutableListOf()
        for (p in points) {
            mPoints.add(p)
        }
    }

    constructor(other: Path): this(other.mPoints.toMutableList(), other.reversed)

    fun getPose(index: Int): Pose2dWithCurvature {
        if (index >= mPoints.size || index < 0) {
            throw IndexOutOfBoundsException("Point $index not in path")
        }
        return mPoints.get(index)
    }

    fun findClosestPointIndex(point: Vector2, lastIndex: Int): Int {
        val lastPose: Vector2 = mPoints.get(lastIndex).translation
        var minDistance: Double = Vector2.distanceBetween(point, lastPose)
        var index: Int = lastIndex
        for (i in lastIndex..mPoints.size - 1) {
            val tempDistance: Double = Vector2.distanceBetween(point, mPoints.get(i).translation)
            if (tempDistance < minDistance) {
                index = i
                minDistance = tempDistance
            }
        }
        return index
    }

    override fun toString(): String {
        val buffer: StringBuilder = StringBuilder()
        for (i in 0..mPoints.size - 1) {
            buffer.append(mPoints.get(i).toString())
            buffer.append(System.lineSeparator())
        }
        return buffer.toString()
    }

    override fun toCSV(): String {
        val buffer: StringBuilder = StringBuilder()
        for (pose in mPoints) {
            buffer.append(pose.toCSV())
            buffer.append(System.lineSeparator())
        }
        return buffer.toString()
    }
}
