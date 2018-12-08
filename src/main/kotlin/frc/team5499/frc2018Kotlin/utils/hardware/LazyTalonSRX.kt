package frc.team5499.frc2018Kotlin.utils.hardware

import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.DemandType
import com.ctre.phoenix.motorcontrol.NeutralMode

class LazyTalonSRX(deviceNumber: Int) : TalonSRX(deviceNumber) {

    protected var lastSet = Double.NaN
    protected var lastSecondarySet = Double.NaN
    protected var lastControlMode: ControlMode? = null
    protected var lastDemandType: DemandType? = null
    protected var lastBrakeMode: NeutralMode? = null

    public override fun set(mode: ControlMode, value: Double) {
        if (value != lastSet || mode != lastControlMode) {
            lastSet = value
            lastControlMode = mode
            lastSecondarySet = Double.NaN
            lastDemandType = null
            super.set(mode, value)
        }
    }

    public override fun set(mode: ControlMode, value0: Double, demandType: DemandType, value1: Double) {
        @Suppress("ComplexCondition")
        if (value0 != lastSet || mode != lastControlMode ||
            demandType != lastDemandType || value1 != lastSecondarySet) {
            lastSet = value0
            lastControlMode = mode
            lastDemandType = demandType
            lastSecondarySet = value1
            super.set(mode, value0, demandType, value1)
        }
    }

    public override fun setNeutralMode(brakeMode: NeutralMode) {
        if (lastBrakeMode != brakeMode) {
            lastBrakeMode = brakeMode
            super.setNeutralMode(brakeMode)
        }
    }
}
