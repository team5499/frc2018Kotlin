package frc.team5499.frc2018Kotlin.auto.actions

import frc.team5499.frc2018Kotlin.subsystems.Drivetrain

// ADD 'TYPE: TURNTYPE' WHEN ABOSUTLE TURN IS INITIALIZED
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
        if (super.timedOut() || Drivetrain.turnError < 3) {
            return true
        }
        return false
    }

    public override fun reset() {
        super.reset()
    }
}
