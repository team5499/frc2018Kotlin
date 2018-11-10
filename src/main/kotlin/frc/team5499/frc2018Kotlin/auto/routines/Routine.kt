package frc.team5499.frc2018Kotlin.auto.routines

import java.util.ArrayList;
import java.util.List;

import frc.team5499.frc2018Kotlin.actions.Action


public class Routine(name: String) {
    public val CURRENT_ROUTINE: RoutineTag = RoutineTag("__default__", -1)
    public val NOT_FINISHED: RoutineTag = RoutineTag("", -1)

    var m_actions: ArrayList<Action>
    var m_step_number: Int
    var name: String

    init {
        m_actions = ArrayList<Action>()
        m_step_number = 0
        this.name = name

    }

    public fun getName (): String {
        return name
    }

    public fun addAction (action: Action) {
        m_actions.add(action)
    }

    public fun  getCurrentAction (): Int {
        return m_actions.get(m_step_number)
    }

    public fun getActionIndex (): Int {
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

    public fun reset () {
        for (i: Action in m_actions) {
            i.reset()
        }
        m_step_number = 0
    }

    public fun isLastStep (): Boolean {
        return (m_step_number >= (m_actions.size() - 1))
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