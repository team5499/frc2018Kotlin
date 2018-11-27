package frc.team5499.frc2018Kotlin.path

import java.text.DecimalFormat

import frc.team5499.frc2018Kotlin.utils.math.geometry.Vector2

class Path(points: List<Vector2>, velocities: List<Double>, reverse: Boolean) {

    private val points: List<Vector2>
    private val velocities: List<Double>

    public val isReverse: Boolean
        get() = field

    init {
        this.points = points
        this.velocities = velocities
        this.isReverse = reverse

        if (this.points.size != this.velocities.size)
            throw ArithmeticException("Velocities and Coordinates do not have same length!")
    }

    fun getPoint(index: Int): Vector2 {
        if (index >= points.size || index < 0) {
            throw IndexOutOfBoundsException("Desired Index ($index) does not exist.")
        }
        return Vector2(points.get(index))
    }

    fun getPointVelocity(index: Int): Double {
        if (index >= points.size || index < 0) {
            throw IndexOutOfBoundsException("Desired Index ($index) does not exist.")
        }
        return velocities.get(index)
    }

    fun getClosestPointIndex(point: Vector2, lastIndex: Int): Int {
        val lastClosest = Vector2(points.get(lastIndex))
        var minDistance = lastClosest.distanceTo(point)
        var index = lastIndex
        for (i in lastIndex..points.size) {
            val tempDistance = point.distanceTo(points.get(i))
            if (tempDistance < minDistance) {
                index = i
                minDistance = tempDistance
            }
        }
        return index
    }

    fun getNumberOfPointsInPath(): Int {
        return points.size
    }

    override fun toString(): String {
        val builder = StringBuilder()
        val format = DecimalFormat("###0.0#")
        for (i in points.indices) {
            builder.append("Point $i" +
                " - Coords: ${points.get(i)}" +
                " - Velo: ${format.format(velocities.get(i))} inches/second \n")
        }
        return builder.toString()
    }
}
