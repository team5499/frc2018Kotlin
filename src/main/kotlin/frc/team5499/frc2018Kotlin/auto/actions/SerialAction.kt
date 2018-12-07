
package frc.team5499.frc2018Kotlin.auto.actions

public class SerialAction(timeout: Double, actions: MutableList<Action>) : Action(timeout) {
    private var childActions: MutableList<out Action>
    private var index: Int = 0

    init {
        childActions = actions
    }

    public override fun start() {
        this.index = 0
        super.start()
    }

    public override fun update() {
        if (childActions[index].next()) {
            index++
            childActions[index].start()
        }
        childActions[index].update()
    }

    public override fun next(): Boolean {
        return super.next()
    }

    public override fun finish() {
        super.finish()
    }
}
