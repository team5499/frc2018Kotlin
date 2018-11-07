package frc.team5499.frc2018Kotlin

object Constants {
    object Talons {
        const val TALON_UPDATE_PERIOD_MS: Int = 1

        const val LEFT_MASTER_PORT: Int = 1
        const val LEFT_SLAVE_PORT: Int = 2
        const val RIGHT_MASTER_PORT: Int = 3
        const val RIGHT_SLAVE_PORT: Int = 4
    }

    object Gyro {
        const val GYRO_PORT_NUMBER: Int = 5
    }

    object Input {
        const val DRIVER_PORT: Int = 1
        const val CODRIVER_PORT: Int = 2
    }

    object Arm{
        const val INTAKE_SPEED: Double = -0.7 
        const val INTAKE_HOLD_SPEED: Double = -0.3
        const val INTAKE_SPIT_SPEED: Double = 0.7
        const val MAX_ARM_SPEED: Double = 0.7  
        const val ARM_PORT: Int = 5
        const val LEFT_INTAKE_PORT: Int = 4
        const val RIGHT_INTAKE_PORT: Int = 6

        const val INVERT_ARM: Boolean = false
        const val INVERT_INTAKE: Boolean = false
    }

    const val EPSILON = 0.00000000001
    const val ENCODERS_TICKS_PER_ROTATION = 1028
    const val WHEEL_DIAMETER = 6.0 // inches
    const val WHEEL_CIR = WHEEL_DIAMETER * 2.0

}