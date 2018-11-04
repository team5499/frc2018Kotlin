
package org.team5499.robots.frc2018.path_pursuit;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.team5499.robots.frc2018.exceptions.IncompletePathException;
import org.team5499.robots.frc2018.math.Vector2d;


public class Path {
    
    private var lines: BoundLine[]? = null
    private var waypoints: Vector2d[]? = null

    public Path(Vector2d[] waypoints) throws IncompletePathException {
        this.lines = new BoundLine[waypoints.length - 1];
        this.waypoints = waypoints;

        if(waypoints.length <= 1) throw new IncompletePathException("Path was provided less than 2 waypoints!");

        for(int i = 0; i < waypoints.length - 1; i++) {
            lines[i] = new BoundLine(waypoints[i], waypoints[i+1]);
        }
    }