package frc.team5499.frc2018Kotlin.auto.actions

class ParallelAction(timeout: Double, actions: MutableList<Action>) : Action(timeout) {

    var mActions: MutableList<Action> = actions

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

    override fun reset() {
        super.reset()
        for (a: Action in mActions) {
            a.reset()
        }
    }
}
