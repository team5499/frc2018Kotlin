package frc.team5499.frc2018Kotlin.utils.math.physics

import frc.team5499.frc2018Kotlin.Constants

class DCMotorTransmission(
    speedPerVolt: Double,
    torquePerVolt: Double,
    frictionVoltage: Double
) {

    public val speedPerVolt: Double
        get() = field
    public val torquePerVolt: Double
        get() = field
    public val frictionVoltage: Double
        get() = field

    init {
        this.speedPerVolt = speedPerVolt
        this.torquePerVolt = torquePerVolt
        this.frictionVoltage = frictionVoltage
    }

    @Suppress("ReturnCount")
    public fun freeSpeedAtVoltage(voltage: Double): Double {
        if (voltage > Constants.EPSILON) {
            return Math.max(0.0, voltage - frictionVoltage) * speedPerVolt
        } else if (voltage < Constants.EPSILON) {
            return Math.min(0.0, voltage + frictionVoltage) * speedPerVolt
        } else {
            return 0.0
        }
    }

    public fun getTorqueForVoltage(outputSpeed: Double, voltage: Double): Double {
        var effectiveVoltage = voltage
        if (outputSpeed > Constants.EPSILON) {
            // Forward motion, rolling friction.
            effectiveVoltage -= frictionVoltage
        } else if (outputSpeed < -Constants.EPSILON) {
            // Reverse motion, rolling friction.
            effectiveVoltage += frictionVoltage
        } else if (voltage > Constants.EPSILON) {
            // System is static, forward torque.
            effectiveVoltage = Math.max(0.0, voltage - frictionVoltage)
        } else if (voltage < -Constants.EPSILON) {
            // System is static, reverse torque
            effectiveVoltage = Math.min(0.0, voltage + frictionVoltage)
        } else {
            // system is idle
            return 0.0
        }
        return torquePerVolt * ((-outputSpeed / speedPerVolt) + effectiveVoltage)
    }

    public fun getVoltageForTorque(outputSpeed: Double, torque: Double): Double {
        val fv: Double
        if (outputSpeed > Constants.EPSILON) {
            // Forward motion, rolling friction.
            fv = frictionVoltage
        } else if (outputSpeed < -Constants.EPSILON) {
            // Reverse motion, rolling friction.
            fv = -frictionVoltage
        } else if (torque > Constants.EPSILON) {
            // System is static, forward torque.
            fv = frictionVoltage
        } else if (torque < -Constants.EPSILON) {
            // System is static, reverse torque.
            fv = -frictionVoltage
        } else {
            // System is idle.
            return 0.0
        }
        return torque / torquePerVolt + outputSpeed / speedPerVolt + fv
    }
}
