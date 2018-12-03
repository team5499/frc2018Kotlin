package frc.team5499.frc2018Kotlin.auto.routines

import frc.team5499.frc2018Kotlin.auto.actions.Action

import frc.team5499.frc2018Kotlin.utils.math.geometry.Rotation2d

class Routine(name: String, startHeading: Rotation2d, vararg actions: Action) {

    val actions: MutableList<Action>
    var stepNumber: Int
        private set
        get() = field
    val mName: String
        get() = field
    val startHeading: Rotation2d
        get() = field

    init {
        stepNumber = 0
        mName = name
        this.startHeading = startHeading
        this.actions = actions.toMutableList()
    }

    @Suppress("SpreadOperator")
    constructor(
        name: String,
        degreesHeading: Double,
        vararg actions: Action
    ): this(name, Rotation2d.fromDegrees(degreesHeading), *actions)

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

    public fun reset() {
        stepNumber = 0
    }

    public fun isLastStep(): Boolean {
        return (stepNumber >= (actions.size - 1))
    }
}
