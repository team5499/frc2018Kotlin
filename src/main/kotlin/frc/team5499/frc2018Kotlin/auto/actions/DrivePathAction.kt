package frc.team5499.frc2018Kotlin.auto.actions

import frc.team5499.frc2018Kotlin.subsystems.Drivetrain
import frc.team5499.frc2018Kotlin.Constants

import org.team5499.monkeyLib.path.Path
import org.team5499.monkeyLib.path.PathFollower
import org.team5499.monkeyLib.auto.Action

public class DrivePathAction(path: Path, timeout: Double) : Action(timeout) {

    private val mPath: Path
    private var mPathFollower: PathFollower?

    init {
        mPath = path
        mPathFollower = null
    }

    public override fun start() {
        mPathFollower = PathFollower(mPath, Constants.TRACK_WIDTH, Constants.Path.LOOK_AHEAD_DISTANCE)
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
        Drivetrain.setVelocity(0.0, 0.0)
    }
}
