package frc.team5499.frc2018Kotlin.auto.actions

import frc.team5499.frc2018Kotlin.auto.routines.Routines
import edu.wpi.first.wpilibj.Timer

open public class Action (timeout_s: Double) {
    private var _timeout: Timer = Timer()
    private var _timeout_ms: Double = timeout_s

    open public fun start () {
        _timeout.reset()
        _timeout.start()
    }

    open public fun update () {}

    protected fun timedOut(): Boolean {
        val t: Double = _timeout.get()
        return (t >= _timeout_ms)
    }

    open public fun next(): Boolean {
        return timedOut()
    }

    open public fun finish() {}

    open public fun reset () {
        _timeout.stop()
        _timeout.reset()
    }
}