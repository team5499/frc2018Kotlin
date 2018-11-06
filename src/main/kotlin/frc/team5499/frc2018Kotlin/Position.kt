package frc.team5499.frc2018Kotlin

object Position {

    private var x = 0.0
    private var y = 0.0
    private var last_left = 0.0
    private var last_right = 0.0
    private var last_angle = 0.0

    fun update(left_distance: Double, right_distance: Double, angle: Double) {
        val newAngle = Math.toRadians(angle)
        var angle_delta = angle - last_angle
        if (angle_delta == 0.0) angle_delta = Constants.EPSILON
        val left_delta = left_distance - last_left
        val right_delta = right_distance - last_right
        val distance = (left_delta + right_delta) / 2.0
        val radius_of_curvature = distance / angle_delta
        val delta_y = radius_of_curvature * Math.sin(angle_delta)
        val delta_x = radius_of_curvature * (Math.cos(angle_delta) - 1)
        y -= delta_x * Math.cos(last_angle) - delta_y * Math.sin(last_angle)
        x += delta_x * Math.sin(last_angle) + delta_y * Math.cos(last_angle)
        last_left = left_distance
        last_right = right_distance
        last_angle = angle
    }

    fun getPositionVector() {}

    fun reset() {
        x = 0.0
        y = 0.0
        last_left = 0.0
        last_right = 0.0
        last_angle = 0.0
    }

    override fun toString(): String {
        return "Robot Position -- X: $x, Y: $y"
    }
}
