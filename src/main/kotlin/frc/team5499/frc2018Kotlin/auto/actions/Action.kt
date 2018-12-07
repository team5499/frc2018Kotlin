package frc.team5499.frc2018Kotlin.auto.actions

import edu.wpi.first.wpilibj.Timer

public open class Action(timeoutSeconds: Double) {
    private var mTimer: Timer = Timer()
    private var mTimeoutSeconds: Double = timeoutSeconds

    public open fun start() {
        mTimer.stop()
        mTimer.reset()
        mTimer.start()
    }

    public open fun update() {}

    protected fun timedOut(): Boolean {
        val t: Double = mTimer.get()
        return (t >= mTimeoutSeconds)
    }

    public open fun next(): Boolean {
        return timedOut()
    }

    public open fun finish() {}
}
