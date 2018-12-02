package frc.team5499.frc2018Kotlin.auto.routines

import frc.team5499.frc2018Kotlin.auto.actions.Action

open class Routine(name: String) {

    val actions: MutableList<Action> = mutableListOf<Action>()
    var stepNumber: Int
        private set
        get() = field
    val mName: String
        get() = field

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
}
