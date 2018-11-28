package frc.team5499.frc2018Kotlin.path

import java.io.FileNotFoundException
import java.io.FileReader

import com.opencsv.CSVReader

import frc.team5499.frc2018Kotlin.Constants
import frc.team5499.frc2018Kotlin.utils.Vector2

@SuppressWarnings("MagicNumber")

public class Path(filepath: String, backwards: Boolean = false) {

    private var mCoordinates: MutableList<Vector2> = mutableListOf<Vector2>()
    private var mTargetVelocities: MutableList<Double> = mutableListOf<Double>()

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
                mCoordinates[i] = tempCoords.get(i)
                mTargetVelocities[i] = tempVelo.get(i)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            // defaults to baseline auto

            mCoordinates.add(Vector2(0.0, 0.0))

            mCoordinates.add(Vector2(0.0, 100.0))

            mTargetVelocities.add(10.0)
        }

        // extend the last line segment by the lookahead distance

        var lastSegmentUnitDirection: Vector2 =
            (mCoordinates[mCoordinates.size - 2] - mCoordinates[mCoordinates.size - 3]).normalized
        mCoordinates[mCoordinates.size - 1] =
            mCoordinates[mCoordinates.size - 2] + (lastSegmentUnitDirection * Constants.LOOK_AHEAD_DISTANCE)

        var pointDistance: Double = Vector2.distanceBetween(
            mCoordinates[mCoordinates.size - 3], mCoordinates[mCoordinates.size - 2])

        mTargetVelocities[mTargetVelocities.size - 2] =
            mTargetVelocities[mTargetVelocities.size - 3] *
            pointDistance / (pointDistance + Constants.LOOK_AHEAD_DISTANCE)
    }

    public fun getPoint(index: Int): Vector2 {
        if (index >= mCoordinates.size || index < 0) {
            throw IndexOutOfBoundsException()
        }
        return mCoordinates[index]
    }

    public fun getPointVelocity(index: Int): Double {
        if (index >= mTargetVelocities.size || index < 0) {
            throw IndexOutOfBoundsException()
        }
        return mTargetVelocities[index]
    }

    public fun findClosestPointIndex(point: Vector2, lastIndex: Int): Int {
        var lastClosest: Vector2 = mCoordinates[lastIndex]
        var minDistance: Double = Vector2.distanceBetween(point, lastClosest)
        var index: Int = lastIndex
        for (i in lastIndex..mCoordinates.size) {
            var tempDistance: Double = Vector2.distanceBetween(point, mCoordinates[i])
            if (tempDistance < minDistance) {
                index = i
                minDistance = tempDistance
            }
        }
        return index
    }

    public override fun toString(): String {
        var tmp: String = ""
        for (i in mCoordinates.indices) {
            tmp += mCoordinates[i].toString() + " - " + mTargetVelocities[i] + "\n"
        }
        return tmp
    }

    public fun getCoordinatesLength(): Int {
        return mCoordinates.size
    }
}
