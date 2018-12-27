package frc.team5499.frc2018Kotlin.auto.actions

import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

import frc.team5499.frc2018Kotlin.Constants

import org.team5499.monkeyLib.auto.Action

public class TurnAction(timeout: Double, angle: Double) : Action(timeout) {

    public enum class TurnType {
        ABOSLUTE,
        RELATIVE
    }

    val mAngle: Double

    init {
        mAngle = angle
        // var mType: TurnType = type
    }

    public override fun start() {
        super.start()
        Drivetrain.setTurn(mAngle)
    }

    public override fun update() {
        super.update()
    }

    public override fun next(): Boolean {
        @Suppress("MagicNumber")
        if (super.timedOut() ||
            (Math.abs(Drivetrain.turnError) < Constants.PID.ACCEPTABLE_TURN_ERROR &&
            Math.abs(Drivetrain.averageVelocity) < Constants.PID.ACCEPTABLE_VELOCITY_THRESHOLD)) {
            return true
        }
        return false
    }
}
