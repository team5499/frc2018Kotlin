package frc.team5499.frc2018Kotlin.auto.actions

import frc.team5499.frc2018Kotlin.subsystems.Drivetrain
import frc.team5499.frc2018Kotlin.path.Path
import frc.team5499.frc2018Kotlin.path.PathFollower

public class DrivePathAction(path: Path, timeout: Double) : Action(timeout) {

    private val mPath: Path
    private var mPathFollower: PathFollower?

    init {
        mPath = path
        mPathFollower = null
    }

    public override fun start() {
        mPathFollower = PathFollower(mPath)
        val output = mPathFollower!!.update(Drivetrain.pose)
        Drivetrain.setVelocity(output.leftVelocity, output.rightVelocity)
    }

    public override fun update() {
        val output = mPathFollower!!.update(Drivetrain.pose)
        Drivetrain.setVelocity(output.leftVelocity, output.rightVelocity)
    }

    public override fun next(): Boolean {
        if (mPathFollower!!.doneWithPath(Drivetrain.pose) || super.next())
            return true
        return false
    }

    public override fun finish() {
        mPathFollower = null
    }

    public override fun reset() {
        mPathFollower = null
    }
}
