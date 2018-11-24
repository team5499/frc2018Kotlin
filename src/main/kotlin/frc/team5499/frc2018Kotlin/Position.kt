package frc.team5499.frc2018Kotlin

import frc.team5499.frc2018Kotlin.utils.Vector2

object Position {

    private var x = 0.0
    private var y = 0.0
    private var lastLeft = 0.0
    private var lastRight = 0.0
    private var lastAngle = 0.0

    var positionVector: Vector2
        get() = Vector2(x, y)
        set(value) {
            x = value.x
            y = value.y
        }

    fun update(leftDistance: Double, rightDistance: Double, angle: Double) {
        val newAngle = Math.toRadians(angle)
        var angle_delta = newAngle - lastAngle
        if (angle_delta == 0.0) angle_delta = Constants.EPSILON
        val leftDelta = leftDistance - lastLeft
        val rightDelta = rightDistance - lastRight
        val distance = (leftDelta + rightDelta) / 2.0
        val radius_of_curvature = distance / angle_delta
        val dy = radius_of_curvature * Math.sin(angle_delta)
        val dx = radius_of_curvature * (Math.cos(angle_delta) - 1)
        y -= dx * Math.cos(lastAngle) - dy * Math.sin(lastAngle)
        x += dx * Math.sin(lastAngle) + dy * Math.cos(lastAngle)
        lastLeft = leftDistance
        lastRight = rightDistance
        lastAngle = newAngle
    }

    fun reset() {
        x = 0.0
        y = 0.0
        lastLeft = 0.0
        lastRight = 0.0
        lastAngle = 0.0
    }

    override fun toString(): String {
        return "Robot Position -- X: $x, Y: $y"
    }
}
