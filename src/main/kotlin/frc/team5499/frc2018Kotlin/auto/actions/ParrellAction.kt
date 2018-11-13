package frc.team5499.frc2018Kotlin.auto.actions

public class ParrellAction(actions: List<Action>, timeout: Double) : Action(timeout) {

    init{
        var mActions: List<Action> = actions
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