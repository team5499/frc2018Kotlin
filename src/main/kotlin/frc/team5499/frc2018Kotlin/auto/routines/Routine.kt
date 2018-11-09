/*package frc.team5499.frc2018Kotlin.auto.routines

import java.util.ArrayList;
import java.util.List;

import org.frc2018.auto.actions.Action

public class Routine {
    companion object{
        val public CURRENT_ROUTINE: RoutineTag = RoutineTag("__default__", -1)
        val public NOT_FINISHED: RoutineTag = RoutineTag("", -1)
    }

    var m_actions: List<Action>
    var m_step_number: Int
    var name: String

    public fun Routine(String name){
        m_actions = ArrayList<Action>()
        m_step_number = 0
        this.name = name
    }

    public fun getName():String{
        return name
    }

    public fun addAction(action:Action){
        m_actions.add(action)
    }

}*/