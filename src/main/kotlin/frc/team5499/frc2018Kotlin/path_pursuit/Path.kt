package org.team5499.robots.frc2018Kotlin.path_pursuit 

import java.io.BufferedReader 
import java.io.FileNotFoundException 
import java.io.FileReader 
import java.util.ArrayList 
import java.util.Arrays 
import java.util.List 

import org.team5499.robots.frc2018Kotlin.exceptions.IncompletePathException 
import org.team5499.robots.frc2018Kotlin.math.Vector2d 

public class Path {

    private var lines : Array<BoundLine>
    private var waypoints : Array<Vector2>

    public fun Path (waypoints : Array<Vector2>) {
        this.lines = new Array<Boundline>(waypoints.length - 1)
        this.waypoints = waypoints

        if(waypoints.length <= 1) throw new IncompletePathException("Path that was provided had less than 2 waypoints!") 

        for (i in waypoints) {
            lines[i] = new BoundLine(waypoints[i]; waypoints[i + 1]) 
            
        }
    }
}

     public fun Vector2d getClosestPointOnLineToPoint(target: Vector2, sample_size: Int) {
        minPoints:Array<Vector2> = new Array<Vector2>(lines.length) 
		for(i in minPoints.length) {
			minPoints[i] = lines[i].getClosestPointOnLineToPoint(target, sample_size) 
		}
		
		minDistance: Double = Double.MAX_VALUE 
		var closestPoint: Vector2 = minPoints[0] 
		for(var point:Vector2 in minPoints) {
			if(Vector2.distanceBetween(point, target) < minDistance) {
				minDistance = Vector2d.distanceBetween(point, target) 
				closestPoint = point 
			}
		}
		
		return closestPoint 
    }

    public fun Int getBoundLineIndexOfClosestPointOnLineToPoint(target: Vector2, sample_size: Int) {
        minPoints: Array<Vector2> = new Vector2[lines.length] 
		for(i in minPoints) {
			minPoints[i] = lines[i].getClosestPointOnLineToPoint(target, sample_size) 
            i++ 
		}
		
		closestIndex: Int = 0 
		minDistance: Double = Double.MAX_VALUE 
		closestPoint: Vector2 = minPoints[0] 
		for(i in minPoints) {
			if(Vector2d.distanceBetween(minPoints[i], target) < minDistance) {
				minDistance = Vector2d.distanceBetween(minPoints[i], target) 
				closestPoint = minPoints[i] 
				closestIndex = i 
			}
		}
		
		return closestIndex 
    }

    public fun Vector2 getLookaheadPointFromPoint(currentPose:Vector2, lookahead_distance: Double, sample_size: Int) {
        currentIndex: Int = getBoundLineIndexOfClosestPointOnLineToPoint(currentPose, sample_size) 
		currentLine: BoundLine = lines[currentIndex] 
		start: Vector2= getClosestPointOnLineToPoint(currentPose, sample_size) 
		
		try {
			remainingDistance: Double = lookahead_distance - Vector2.distanceBetween(start, waypoints[currentIndex+1]) 
			if(remainingDistance > 0) {
				lowerBound: Double= lines[currentIndex+1].getLowerBound() 
				upperBound: Double = lines[currentIndex+1].getUpperBound() 
				stepSize: Double= (upperBound - lowerBound) / ((double) sample_size) 
				xValue: Double= lowerBound + (2 * stepSize) 
				return getLookaheadPointFromPoint(new Vector2(xValue, lines[currentIndex+1].get(xValue)), remainingDistance, sample_size) 
			} else if(remainingDistance == 0) {
				return waypoints[currentIndex+1] 
			} else {
                startingVector: Vector2= new Vector2(start) 
				startingVector.multiply(-1) 

                deltaVector: Vector2= new Vector2(waypoints[currentIndex+1]) 
				deltaVector.add(startingVector) 
				deltaVector.multiply(lookahead_distance / Vector2.distanceBetween(start, waypoints[currentIndex+1])) 

                finalVector: Vector2 = new Vector2(start) 
				finalVector.add(deltaVector) 
				return finalVector 
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			return waypoints[waypoints.length-1] 
		}
    }

    public fun Double getDistanceToEndpoint(position: Vector2) {
        return Vector2.distanceBetween(position, waypoints[waypoints.length - 1])
    }

    public fun Array<BoundLine> getBoundLines() {
        return this.lines
    }

    public fun Array<Vector2> getWayPoints() {
        return this.waypoints
    }
    
    public static fun Path readPathFromFile(String path) {
        List<Vector2d> waypoints = new ArrayList<>(5)
        try {
            BufferedReader br = new BufferedReader(new FileReader(path)) 
            String line = br.readLine()
            while(line != null) {
                String[] s = line.split(",")
                if(s.length > 2) {
                    throw new Exception("Too many arguments in path") 
                }
                x: Double = Double.parseDouble(s[0].trim()) 
                y: Double = Double.parseDouble(s[1].trim()) 
                waypoints.add(new Vector2d(x, y)) 
                line = br.readLine() 
            }
            br.close() 
        } catch(Exception e) {
            e.printStackTrace() 
            return default_path 
        }
        
        res: Array<Vector2> = new Vector2[waypoints.size()] 
        res = waypoints.toArray(res) 
        return new Path(res) 
    }

    private static fun Path createDefaultPath() {
        Array<Vector2> waypoints = {new Vector2d(0,0), new Vector2d(1, 1)} 
        return new Path(waypoints) 
    }

    private static default_path: Path = createDefaultPath() 

} 