package frc.team5499.frc2018Kotlin.auto.routines

import frc.team5499.frc2018Kotlin.auto.actions.Action


open class Routine(name: String) {
    companion object{
        public val CURRENTROUTINE: RoutineTag = RoutineTag("__default__", -1)
        public val NOT_FINISHED: RoutineTag = RoutineTag("", -1)
    }

    var m_actions: MutableList<Action> = mutableListOf<Action>()
    var m_step_number: Int = 0
    var m_name: String = name
   
    public fun getName(): String {
        return m_name
    }

    public fun addAction(action: Action) {
        m_actions.add(action)
    }

    public fun getCurrentAction(): Action {
        return m_actions.get(m_step_number)
    }

    public fun getActionIndex(): Int {
        return m_step_number
    }

    public fun advanceRoutine (): Boolean {
        if(isLastStep()){
            return false
        }
        m_step_number++
        return true
    }

    public fun setActionIndex (index: Int) {
        m_step_number = index
    }

    open public fun reset () {
        for (i: Action in m_actions) {
            i.reset()
        }
        m_step_number = 0
    }

    public fun isLastStep (): Boolean {
        return (m_step_number >= (m_actions.size - 1))
    }

    class RoutineTag (const_name: String, const_index: Int) {
        private var name: String = const_name 
        private var index: Int = const_index

        public fun getName(): String {
            return name
        } 

        public fun getIndex(): Int {
            return index
        }

        public fun equals(o: RoutineTag): Boolean {
            return (o.getName() == getName() && o.getIndex() == getIndex())
        }
    }
}