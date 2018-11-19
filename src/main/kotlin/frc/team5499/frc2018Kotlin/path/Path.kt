package frc.team5499.frc2018Kotlin.path

import java.io.FileNotFoundException
import java.io.FileReader

import com.opencsv.CSVReader

import frc.team5499.frc2018Kotlin.Constants
import frc.team5499.frc2018Kotlin.utils.Vector2

public class Path(filepath: String, backwards: Boolean = false) {

    private var coordinates: MutableList<Vector2> = mutableListOf<Vector2>()
    private var targetVelocities: MutableList<Double> = mutableListOf<Double>()

    private var backwards: Boolean = false

    init {
        this.backwards = backwards

        var tempCoords: MutableList<Vector2> = mutableListOf<Vector2>()
        var tempVelo: MutableList<Double> = mutableListOf<Double>()
        try {
            var reader: CSVReader = CSVReader(FileReader(filepath))
            var line: Array<String> = reader.readNext()
            while (line != null) {
                tempCoords.add(Vector2(line[0].toDouble(), line[1].toDouble()))
                if (backwards) { tempVelo.add(-line[2].toDouble()) } else { tempVelo.add(line[2].toDouble()) }
                line = reader.readNext()
            }
            reader.close()

            for (i in tempCoords.indices) {
                coordinates[i] = tempCoords.get(i)
                targetVelocities[i] = tempVelo.get(i)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            System.exit(1)
        }

        // extend the last line segment by the lookahead distance
        @Suppress("MagicNumber")
        var lastSegmentUnitDirection: Vector2 = Vector2.unitDirectionVector(
            coordinates[coordinates.size - 2] - coordinates[coordinates.size - 3])
        coordinates[coordinates.size - 1] =
            coordinates[coordinates.size - 2] + (lastSegmentUnitDirection * Constants.LOOK_AHEAD_DISTANCE)
        @Suppress("MagicNumber")
        var pointDistance: Double = Vector2.distanceBetween(
            coordinates[coordinates.size - 3], coordinates[coordinates.size - 2])
        @Suppress("MagicNumber")
        targetVelocities[targetVelocities.size - 2] =
            targetVelocities[targetVelocities.size - 3] *
            pointDistance / (pointDistance + Constants.LOOK_AHEAD_DISTANCE)
    }

    public fun getPoint(index: Int): Vector2 {
        if (index >= coordinates.size || index < 0) {
            throw IndexOutOfBoundsException()
        }
        return Vector2.copyVector(coordinates[index])
    }

    public fun isBackwards(): Boolean {
        return backwards
    }

    public fun getPointVelocity(index: Int): Double {
        if (index >= targetVelocities.size || index < 0) {
            throw IndexOutOfBoundsException()
        }
        return targetVelocities[index]
    }

    public fun findClosestPointIndex(point: Vector2, lastIndex: Int): Int {
        var lastClosest: Vector2 = coordinates[lastIndex]
        var minDistance: Double = Vector2.distanceBetween(point, lastClosest)
        var index: Int = lastIndex
        for (i in lastIndex..coordinates.size) {
            var tempDistance: Double = Vector2.distanceBetween(point, coordinates[i])
            if (tempDistance < minDistance) {
                index = i
                minDistance = tempDistance
            }
        }
        return index
    }

    public override fun toString(): String {
        var tmp: String = ""
        for (i in coordinates.indices) {
            tmp += coordinates[i].toString() + " - " + targetVelocities[i] + "\n"
        }
        return tmp
    }

    public fun getPathLength(): Int {
        return coordinates.size
    }
}
