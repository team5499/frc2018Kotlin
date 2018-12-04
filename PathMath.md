# Path Generation and Pure Pursuit

## Spline Generation:

We generate our paths using "Parametric Quintic Hermtite Splines." This basically means that for each pair of points, we have a parametric function (different equation for x and y, both are in respect to t). Both the x and y have a 5th degree polynomial in respect to t. Each point has an x coordinate, y coordinate, and desired heading: theta. The initial point (poin that comes earlier) of each segment is represented by t = 0.0 (time = 0) and the end point is t = 1.0 (time = 1). This "time" is completely arbitrary and doesn't mean actual time in seconds. The heading at each point (that's the hermite part of quintic hermite) basically creates a tangent at that point that the parametric function trys to follow for atleast a small ammout of time which allows for more precise robot placement.

Each spline has 6 coefficients for each axis (x and y):

#### For X:
the coefficients are: ax, bx, cx, dx, ex, and fx. leading to a polynomial in the form:

    ax * t^5 + bx * t^4 + cx * t^3 + dx * t^2 + ex * t + fx

#### For Y:
 Same as above. ay, by, etc

    ay * t^5 + by * t^4 + cy * t^3 + dy * t^2 + ey * t + fy


To connect these splines together, we need to make a couple of key assumptions. Assume you have 3 points: p1, p2, and p3, and 2 splines, s1 and s2, conisting of p1-p2 and p2-p3. Since the function is quintic, we must assume that x and y (x and y positions) are equal at t = 1.0 of s1 and t = 0.0 of s2, we must also assume that dx and dy (the x and y velocities) are equal at these times. And since they're quintic, we must assume that ddx and ddy (x and y accelerations) are equal at these times as well, creating a smooth path. At first, we assume that ddx and ddy are 0 at every point, but later we opimize the splines and  change this up a little to make the path more efficient for a robot to follow.

## Spline optimization:

Basically trys to get the points into a striaght line by checking if the points are "colinear." I won't get into the math of this as it doesn't really matter all that much.

## Path Creation:

To create paths we take a array (lets name it "coords") of "poses," a class containing a distancement (x and y) and a rotation (theta). We then loop through the set of poses and create a spline for each pairing. For example (Kotlin FYI):

```
val splines = mutableListOf<QuinticHermiteSpline>()
for (i in 0..coords.length - 2) {
    splines.add(QuinticHermiteSpline(coords.get(i), coords.get(i + 1)))

}
```


These splines are nice and all, but the robot cant follow these directly, they need to be broken up into smaller pieces, into more sets of coordinates. So, we use an algorythm that recursivly takes a segment of the selected spline, starting with a segment form t = 0 to t = 1, and checks if the segment is longer that ~3 inches (this number is configurable). If it is longer, the algorithm calls itself again only using the function from times 0.0 to 0.5 and from 0.5 to 1.0, splitting the segment in half. This continues untill all segments are smaller than the selected size. We find the length using calculus to find the arc length of the current curve. This algorithm will return a list of "Poses with Curvature" which are basically packages with x and y displacement, rotation (theta), and the curvature of the spline at that point in the path. The curvatures are used later to calculate velocity.

If the path is meant to be followed in reverse. We need to flip all the headings of the "poses" to allow for correct interpolation. Otherise there will be no additional points created. We must also negate the curvature, heading, and y coordinate of each pose

Next, the target velocities for the path are created. Currently,we do 3 passes of velocity optimization.

On the first loop we set the max achievable velocity at each point by using (Again Kotlin):

    /// velocities is an array of the target velocities
    velocities[i] = Math.max(MAX_VELOCITY, k / |curvature[i]|)

"k" can be a constant from 1-5 depending of how fast you want your robot to corner. (Lower is slower cornering. We use 3.0).

For the second and third loops we use the kinematic equation (dx stands for delta x not derivative of x):

    vf^2 = vi^2 + 2.0 * a * dx

This simplifies to:

    vf = sqrt(vi^2 + 2.0 * a * dx)

For the second loop, we set the final velocitiy to the desired value (usually 0) and then loop backwards through the velocities array and use this equation to calculate the max desired velocity of the previous point based on the maximum allowed acceleration (also a customizable value per path). Kotlin again:

```
velocities.set(velocities.size - 1, endVelo)

for (i in (samples.size - 2).downTo(0)) {

    val distance = samples.get(i).translation.distanceTo(samples.get(i + 1).translation)

    val value = Math.min(velocities.get(i), Math.sqrt(Math.pow(velocities.get(i + 1), 2.0) + 2.0 * maxAccel * distance))

    velocities.set(i, value)
}

```

And then finally, we set the first value to the desired value (usually around 10 or 20 inches/second) and then loop through forwards, using the same equation.

```
velocities.set(0, startVelo)

for (i in 0..samples.size - 2) {

    val distance = samples.get(i).translation.distanceTo(samples.get(i + 1).translation)

    val value = Math.sqrt(Math.pow(velocities.get(i), 2.0) + 2.0 * maxAccel * distance)

    if (value < velocities.get(i + 1))
        velocities.set(i + 1, value)
}

```

Now the veloctites should be manageable by the robot.

## Path Following:
