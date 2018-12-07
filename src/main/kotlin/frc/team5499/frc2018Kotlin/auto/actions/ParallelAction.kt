package frc.team5499.frc2018Kotlin.auto.actions

class ParallelAction(timeout: Double, vararg actions: Action) : Action(timeout) {

    private val mActions: Array<out Action>

    init {
        mActions = actions.copyOf()
    }

    override fun start() {
        super.start()
        for (a: Action in mActions) {
            a.start()
        }
    }

    override fun update() {
        super.update()
        for (a: Action in mActions) {
            a.update()
        }
    }

    @Suppress("ReturnCount")
    override fun next(): Boolean {
        if (super.timedOut()) {
            return true
        }
        for (a: Action in mActions) {
            if (!a.next()) {
                return false
            }
        }
        return true
    }

    override fun finish() {
        super.finish()
        for (a: Action in mActions) {
            a.finish()
        }
    }
}
