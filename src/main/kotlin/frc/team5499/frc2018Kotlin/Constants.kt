package frc.team5499.frc2018Kotlin

import frc.team5499.frc2018Kotlin.controllers.TeleopController.DriveConfig

object Constants {
    object Talons {
        const val TALON_UPDATE_PERIOD_MS: Int = 1
        const val TALON_PIDF_UPDATE_PERIOD_MS: Int = 1

        const val LEFT_MASTER_PORT: Int = 2
        const val LEFT_SLAVE_PORT: Int = 1
        const val RIGHT_MASTER_PORT: Int = 8
        const val RIGHT_SLAVE_PORT: Int = 9
        const val ARM_PORT: Int = 5
        const val LEFT_INTAKE_PORT: Int = 4
        const val RIGHT_INTAKE_PORT: Int = 6
    }

    object Gyro {
        const val GYRO_PORT_NUMBER: Int = 10
        const val TURN_UNITS_PER_ROTATION: Int = 3600
        const val PIGEON_UNITS_PER_ROTATION: Int = 8192
    }

    object Input {
        const val DRIVER_PORT: Int = 0
        const val CODRIVER_PORT: Int = 1

        // change to change drive mode
        val DRIVE_CONFIG: DriveConfig = DriveConfig.SPACE

        // cheesy
        const val THROTTLE_DEADBAND = 0.02
        const val WHEEL_DEADBAND = 0.02

        const val HIGH_WHEEL_NONLINEARITY = 0.65
        const val LOW_WHEEL_NONLINEARITY = 0.65

        const val HIGH_NEGINERTIA_SCALAR = 4.0
        const val LOW_NEGINERTIA_THRESHOLD = 0.65
        const val LOW_NEGINERTIA_TURN_SCALAR = 3.5
        const val LOW_NEGINERTIA_CLOSE_SCALAR = 4.0
        const val LOW_NEGINERTIA_FAR_SCALAR = 5.0

        const val HIGH_SENSITIVITY = 0.65
        const val LOW_SENSITIVITY = 0.65

        const val QUICKSTOP_DEADBAND = 0.5
        const val QUICKSTOP_WEIGHT = 0.1
        const val QUICKSTOP_SCALAR = 5.0

        // space
        const val TURN_MULT = 0.6

        // tank
        const val SLOW_MULT = 0.4
    }

    object Path {
        const val LOOK_AHEAD_DISTANCE = 18.0 // inches
        const val MAX_ACCELERATION = 50.0 // inches / s / s
        const val MAX_VELOCITY = 50.0 // inches / s
        const val DEFAULT_START_VELOCITY = 10.0
        const val DEFAULT_END_VELOCITY = 0.0
    }

    object PID {
        const val ACCEPTABLE_VELOCITY_THRESHOLD = 3.0 // inches / s
        const val ACCEPTABLE_TURN_ERROR = 3.0 // degrees (?)
        const val ACCEPTABLE_DISTANCE_ERROR = 2.0 // inches

        const val VEL_KP = 2.5
        const val VEL_KI = 0.0
        const val VEL_KD = 0.0
        const val VEL_KF = 0.95
        const val VEL_IZONE = 10
        const val VEL_MAX_OUTPUT = 1.0

        const val POS_KP = 0.79
        const val POS_KI = 0.0
        const val POS_KD = 0.3
        const val POS_KF = 0.0
        const val POS_IZONE = 10
        const val POS_MAX_OUTPUT = 0.5

        const val ANGLE_KP = 2.0
        const val ANGLE_KI = 0.0
        const val ANGLE_KD = 0.0
        const val ANGLE_KF = 0.0
        const val ANGLE_IZONE = 10
        const val ANGLE_MAX_OUTPUT = 1.0

        const val TURN_KP = 1.3
        const val TURN_KI = 0.0
        const val TURN_KD = 12.0
        const val TURN_KF = 0.0
        const val TURN_IZONE = 10
        const val TURN_MAX_OUTPUT = 1.0

        const val FIXED_KP = 0.0
        const val FIXED_KI = 0.0
        const val FIXED_KD = 0.0
        const val FIXED_KF = 0.0
        const val FIXED_IZONE = 10
        const val FIXED_MAX_OUTPUT = 0.5

        const val INVERT_FIXED_AUX_PIDF = true
        const val INVERT_ANGLE_AUX_PIDF = true
        const val INVERT_TURN_AUX_PIDF = false
    }

    object Arm {
        const val INTAKE_SPEED: Double = -0.7
        const val INTAKE_HOLD_SPEED: Double = -0.3
        const val INTAKE_SPIT_SPEED: Double = 0.7
        const val INTAKE_DROP_SPEED: Double = 0.3
        const val MAX_ARM_SPEED: Double = 0.7

        const val ARM_UP_SPEED = 0.8
        const val ARM_DOWN_SPEED = -1.0
        const val ARM_UP_HOLD_SPEED = 0.2
        const val ARM_DOWN_HOLD_SPEED = -0.2

        const val INVERT_ARM: Boolean = false
        const val INVERT_INTAKE: Boolean = false
    }

    const val EPSILON = 1E-10
    const val ENCODER_TICKS_PER_ROTATION = 1024
    const val WHEEL_DIAMETER = 6.0 // inches
    const val WHEEL_CIR = WHEEL_DIAMETER * Math.PI
    const val TRACK_WIDTH = 20.0
    const val MAX_VELOCITY_SETPOINT = 120.0 // inches per second
    const val UPDATE_PERIOD = 0.005
}
