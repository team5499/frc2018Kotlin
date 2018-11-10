package org.frc2018.path

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;

import org.frc2018.Constants;
import org.frc2018.Vector2;
import org.glassfish.grizzly.compression.lzma.impl.Base;

public class Path(filepath: String, backwards: Boolean = false){

    private var coordinates: MutableList<Vector2> = mutableListOf<Vector2>()
    private var target_velocities: MutableList<Double> = mutableListOf<Double>()

    private var backwards: Boolean = false

    init {
        this.backwards = backwards;

        var temp_coords: MutableList<Vector2> = mutableListOf<Vector2>()
        var temp_velo: MutableList<Double> = mutableListOf<Double>()
        try {
            var reader: CSVReader = CSVReader(FileReader(filepath))
            var line: MutableList<String> = reader.readNext()
            while(line != null) {
                temp_coords.add(Vector2(Double.parseDouble(line[0]), Double.parseDouble(line[1])))
                if(backwards) {temp_velo.add(-Double.parseDouble(line[2]))}
                else {temp_velo.add(Double.parseDouble(line[2]))}
                line = reader.readNext()
            }
            reader.close()

            coordinates = mutableListOf<Vector2>(temp_coords.size() + 1)
            target_velocities = mutableListOf<Double>(temp_velo.size() + 1)

            for (i in temp_coords.indicies) {
                coordinates[i] = temp_coords.get(i);
                target_velocities[i] = temp_velo.get(i);
            }
        } 
        catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        // extend the last line segment by the lookahead distance
        var last_segment_unit_direction: Vector2 = Vector2.unitDirectionVector(Vector2.subtract(coordinates[coordinates.length - 2], coordinates[coordinates.length - 3]))
        coordinates[coordinates.length - 1] = Vector2.add(coordinates[coordinates.length - 2], Vector2.multiply(last_segment_unit_direction, Constants.LOOK_AHEAD_DISTANCE))
        var double point_distance: Double = Vector2.distanceBetween(coordinates[coordinates.length - 3], coordinates[coordinates.length - 2])
        target_velocities[target_velocities.length - 2] = target_velocities[target_velocities.length - 3] * point_distance / (point_distance + Constants.LOOK_AHEAD_DISTANCE)
    }

    public fun getPoint(index: Int): Vector2 {
        if(index >= coordinates.length || index < 0) {
            throw IndexOutOfBoundsException()
        }
        return Vector2.copyVector(coordinates[index])
    }

    public fun isBackwards(): Boolean {
        return backwards;
    }

    public fun getPointVelocity(index: Int): Double {
        if(index >= target_velocities.length || index < 0) {
            throw IndexOutOfBoundsException()
        }
        return Double(target_velocities[index])
    }

    public fun findClosestPointIndex(point: Vector2, last_index: Int): Int {
        var last_closest: Vector2 = coordinates[last_index];
        var min_distance: Double = Vector2.distanceBetween(point, last_closest)
        var index: Int = last_index
        for(i in last_index..coordinates.size) {
            var temp_distance: Double = Vector2.distanceBetween(point, coordinates[i])
            if(temp_distance <  min_distance) {
                index = i
                min_distance = temp_distance
            }
        }
        return index;
    }

    @Override
    public fun toString(): String {
        var tmp: String = ""
        for(i in coordinates.indices) {
            tmp += coordinates[i].toString() + " - " + target_velocities[i] + "\n"
        }
        return tmp
    }

    public fun getPathLength(): Int {
        return coordinates.length
    }
}