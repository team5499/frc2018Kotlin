
package frc.team5499.frc2018Kotlin.auto.actions

public class SerialAction(timeout: Double, actions: MutableList) : Action(timeout) {
    private var childActions: MutableList<Action>
    private var index: Int = 0

    init {
        childActions = actions
    }

    public fun start() {
        super.start()
    }

    public fun update() {
        if (childActions[index].next()) {
            index++
            childActions[index].start()
        }
        childActions[index].update()
    }

    protected fun timedOut(): Boolean {
        return childActions[-1].timedOut()
    }

    public fun next(): Boolean {
        return timedOut()
    }

    public fun finish() {
        super.finish()
    }

    public fun reset() {
        super.reset()
    }
}
