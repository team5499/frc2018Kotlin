package frc.team5499.frc2018Kotlin.utils.math.physics

import frc.team5499.frc2018Kotlin.utils.CSVWritable
import frc.team5499.frc2018Kotlin.Constants

import java.text.DecimalFormat

class DifferentialDrive(
    mass: Double,
    moi: Double,
    angularDrag: Double,
    wheelRadius: Double,
    effectiveWheelbaseRadius: Double,
    leftTransmission: DCMotorTransmission,
    rightTransmission: DCMotorTransmission
) {

    // All units must be SI!

    // Equivalent mass when accelerating purely linearly, in kg.
    // This is "equivalent" in that it also absorbs the effects of drivetrain inertia.
    // Measure by doing drivetrain acceleration characterization in a straight line.
    protected val mass: Double
        get() = field

    // Equivalent moment of inertia when accelerating purely angularly, in kg*m^2.
    // This is "equivalent" in that it also absorbs the effects of drivetrain inertia.
    // Measure by doing drivetrain acceleration characterization while turning in place.
    protected val moi: Double
        get() = field

    // Drag torque (proportional to angular velocity) that resists turning, in N*m/rad/s
    // Empirical testing of our drivebase showed that there was an unexplained loss in torque ~proportional to angular
    // velocity, likely due to scrub of wheels.
    // NOTE: this may not be a purely linear term, and we have done limited testing, but this factor helps our model to
    // better match reality.  For future seasons, we should investigate what's going on here...
    protected val angularDrag: Double
        get() = field

    // Self-explanatory.  Measure by rolling the robot a known distance and counting encoder ticks.
    protected val wheelRadius: Double // m
        get() = field

    // "Effective" kinematic wheelbase radius.  Might be larger than theoretical to compensate for skid steer.  Measure
    // by turning the robot in place several times and figuring out what the equivalent wheelbase radius is.
    protected val effectiveWheelbaseRadius: Double // m
        get() = field

    // Transmissions for both sides of the drive.
    protected val leftTransmission: DCMotorTransmission
        get() = field
    protected val rightTransmission: DCMotorTransmission
        get() = field

    init {
        this.mass = mass
        this.moi = moi
        this.angularDrag = angularDrag
        this.wheelRadius = wheelRadius
        this.effectiveWheelbaseRadius = effectiveWheelbaseRadius
        this.leftTransmission = leftTransmission
        this.rightTransmission = rightTransmission
    }

    // Input/demand could be either velocity or acceleration...the math is the same.
    public fun solveForwardKinematics(wheelMotion: WheelState): ChassisState {
        val chassisMotion = ChassisState()
        chassisMotion.linear = wheelRadius * (wheelMotion.right + wheelMotion.left) / 2.0
        chassisMotion.angular = wheelRadius * (wheelMotion.right - wheelMotion.left) / (2.0 *
            effectiveWheelbaseRadius)
        return chassisMotion
    }

    // Input/output could be either velocity or acceleration...the math is the same.
    public fun solveInverseKinematics(chassisMotion: ChassisState): WheelState {
        val wheelMotion = WheelState()
        wheelMotion.left = (chassisMotion.linear - effectiveWheelbaseRadius * chassisMotion.angular) /
            wheelRadius
        wheelMotion.right = (chassisMotion.linear + effectiveWheelbaseRadius * chassisMotion.angular) /
            wheelRadius
        return wheelMotion
    }

    public fun solveForwardDynamics(chassisVelocity: ChassisState, voltage: WheelState): DriveDynamics {
        val dynamics = DriveDynamics()
        dynamics.wheelVelocity = solveInverseKinematics(chassisVelocity)
        dynamics.chassisVelocity = chassisVelocity
        dynamics.curvature = dynamics.chassisVelocity.angular / dynamics.chassisVelocity.linear
        if (dynamics.curvature.isNaN()) dynamics.curvature = 0.0
        dynamics.voltage = voltage
        solveForwardDynamics(dynamics)
        return dynamics
    }

    public fun solveForwardDynamics(dynamics: DriveDynamics) {
        val leftStationary = Math.abs(dynamics.wheelVelocity.left) < Constants.EPSILON &&
            Math.abs(dynamics.voltage.left) < leftTransmission.frictionVoltage
        val rightStationary = Math.abs(dynamics.wheelVelocity.right) < Constants.EPSILON &&
            Math.abs(dynamics.voltage.right) < rightTransmission.frictionVoltage

        if (leftStationary && rightStationary) {
            dynamics.wheelTorque.left = 0.0
            dynamics.wheelTorque.right = 0.0
            dynamics.chassisAcceleration.linear = 0.0
            dynamics.chassisAcceleration.angular = 0.0
            dynamics.wheelAcceleration.left = 0.0
            dynamics.wheelAcceleration.right = 0.0
            dynamics.dcurvature = 0.0
            return
        }

        dynamics.wheelTorque.left = leftTransmission.getTorqueForVoltage(
            dynamics.wheelVelocity.left,
            dynamics.voltage.left)
        dynamics.wheelTorque.right = rightTransmission.getTorqueForVoltage(
            dynamics.wheelVelocity.right,
            dynamics.voltage.right
        )

        dynamics.chassisAcceleration.linear = (dynamics.wheelTorque.left + dynamics.wheelTorque.right) /
            (wheelRadius * mass)

        dynamics.chassisAcceleration.angular = effectiveWheelbaseRadius *
            (dynamics.wheelAcceleration.right - dynamics.wheelTorque.left) / (wheelRadius * moi) -
            dynamics.chassisVelocity.angular * angularDrag / moi

        // dynamics.dcurvature
    }

    // Can refer to velocity or acceleration depending on context.
    public class ChassisState(linear: Double, angular: Double) {
        public var linear: Double
        public var angular: Double

        init {
            this.linear = linear
            this.angular = angular
        }

        public constructor(): this(0.0, 0.0)

        public override fun toString(): String {
            val fmt = DecimalFormat("#0.000")
            return fmt.format(linear) + ", " + fmt.format(angular)
        }
    }

    public class WheelState(left: Double, right: Double) {
        public var left: Double
        public var right: Double

        init {
            this.left = left
            this.right = right
        }

        public constructor(): this(0.0, 0.0)

        public override fun toString(): String {
            val fmt = DecimalFormat("#0.000")
            return fmt.format(left) + ", " + fmt.format(right)
        }
    }

    public class DriveDynamics : CSVWritable {
        public var curvature = 0.0 // m^-1
        public var dcurvature = 0.0 // m^-1/m
        public var chassisVelocity = ChassisState() // m/s
        public var chassisAcceleration = ChassisState() // m/s^2
        public var wheelVelocity = WheelState() // rad/s
        public var wheelAcceleration = WheelState() // rad/s^2
        public var voltage = WheelState() // V
        public var wheelTorque = WheelState() // N m

        public override fun toCSV(): String {
            return ("$curvature, $dcurvature, $chassisVelocity, " +
                "$chassisAcceleration, $wheelVelocity, $wheelAcceleration, $voltage, $wheelTorque")
        }
    }
}
