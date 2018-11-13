package frc.team5499.frc2018Kotlin.auto.actions


public class TurnAction(angle: Double, /*type: TurnType,*/ timeout: Double) : Action(timeout) {

    init{
        var mAngle: Double = angle
        //var mType: TurnType = type
    }

    override public fun start() {
    }

    override public fun update() {
    }

    override public fun next(): Boolean {
        return false
    }

    override public fun finish() {
    }

    override public fun reset() {
    }
}