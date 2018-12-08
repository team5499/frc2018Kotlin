
package frc.team5499.frc2018Kotlin.auto.actions

public class SerialAction(timeout: Double, vararg actions: Action) : Action(timeout) {

    private val childActions: Array<out Action>
    private var index: Int = 0

    init {
        childActions = actions
    }

    public override fun start() {
        this.index = 0
        super.start()
        childActions[index].start()
    }

    public override fun update() {
        if (index == childActions.size - 1) return
        if (childActions[index].next()) {
            childActions[index].finish()
            index++
            childActions[index].start()
        }
        childActions[index].update()
    }

    public override fun next(): Boolean {
        return childActions[childActions.size - 1].next()
    }

    public override fun finish() {
        super.finish()
    }
}
