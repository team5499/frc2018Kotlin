package frc.team5499.frc2018Kotlin.auto.routines

import frc.team5499.frc2018Kotlin.auto.actions.Action

open class Routine(name: String) {
    companion object {
        public val CURRENTROUTINE: RoutineTag = RoutineTag("__default__", -1)
        public val NOT_FINISHED: RoutineTag = RoutineTag("", -1)
    }

    val actions: MutableList<Action> = mutableListOf<Action>()
    var stepNumber: Int
        get() = stepNumber
    val mName: String

    init {
        stepNumber = 0
        mName = name
    }

    public fun addAction(action: Action) {
        actions.add(action)
    }

    public fun getCurrentAction(): Action {
        return actions.get(stepNumber)
    }

    public fun advanceRoutine(): Boolean {
        if (isLastStep()) {
            return false
        }
        stepNumber++
        return true
    }

    public fun setActionIndex(index: Int) {
        stepNumber = index
    }

    public open fun reset() {
        for (i: Action in actions) {
            i.reset()
        }
        stepNumber = 0
    }

    public fun isLastStep(): Boolean {
        return (stepNumber >= (actions.size - 1))
    }

    class RoutineTag(nameIn: String, indexIn: Int) {
        val name: String
        val index: Int

        init {
            name = nameIn
            index = indexIn
        }

        public fun equals(o: RoutineTag): Boolean {
            return (o.name == name && o.index == index)
        }
    }
}
