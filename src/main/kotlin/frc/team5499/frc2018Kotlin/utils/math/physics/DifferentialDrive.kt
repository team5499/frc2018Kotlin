package frc.team5499.frc2018Kotlin.utils.math.physics

import frc.team5499.frc2018Kotlin.utils.CSVWritable
import frc.team5499.frc2018Kotlin.Constants
import frc.team5499.frc2018Kotlin.utils.Utils

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

        // Add forces and torques about the center of mass.
        dynamics.chassisAcceleration.linear = (dynamics.wheelTorque.right + dynamics.wheelTorque.left) /
                (wheelRadius * mass)
        // (Tr - Tl) / r_w * r_wb - drag * w = I * angular_accel
        dynamics.chassisAcceleration.angular = effectiveWheelbaseRadius * (dynamics.wheelTorque.right - dynamics
                .wheelTorque.left) / (wheelRadius * moi) - dynamics.chassisVelocity.angular * angularDrag / moi

        // Solve for change in curvature from angular acceleration.
        // total angular accel = linear_accel * curvature + v^2 * dcurvature
        dynamics.dcurvature = (dynamics.chassisAcceleration.angular -
            dynamics.chassisAcceleration.linear * dynamics.curvature) /
            (dynamics.chassisVelocity.linear * dynamics.chassisVelocity.linear)
        if (dynamics.dcurvature.isNaN()) dynamics.dcurvature = 0.0

        // Resolve chassis accelerations to each wheel.
        dynamics.wheelAcceleration.left = dynamics.chassisAcceleration.linear - dynamics.chassisAcceleration
                .angular * effectiveWheelbaseRadius
        dynamics.wheelAcceleration.right = dynamics.chassisAcceleration.linear + dynamics.chassisAcceleration
                .angular * effectiveWheelbaseRadius
    }

    public fun solveInverseDynamics(chassisVelocity: ChassisState, chassisAcceleration: ChassisState): DriveDynamics {
        val dynamics = DriveDynamics()
        dynamics.chassisVelocity = chassisVelocity
        dynamics.curvature = dynamics.chassisVelocity.angular / dynamics.chassisVelocity.linear
        if (dynamics.curvature.isNaN()) dynamics.curvature = 0.0
        dynamics.chassisAcceleration = chassisAcceleration
        dynamics.dcurvature = (dynamics.chassisAcceleration.angular -
            dynamics.chassisAcceleration.linear * dynamics.curvature) /
            (dynamics.chassisVelocity.linear * dynamics.chassisVelocity.linear)
        if (dynamics.dcurvature.isNaN()) dynamics.dcurvature = 0.0
        dynamics.wheelVelocity = solveInverseKinematics(chassisVelocity)
        dynamics.wheelAcceleration = solveInverseKinematics(chassisAcceleration)
        solveInverseDynamics(dynamics)
        return dynamics
    }

    public fun solveInverseDynamics(wheelVelocity: WheelState, wheelAcceleration: WheelState): DriveDynamics {
        val dynamics = DriveDynamics()
        dynamics.chassisVelocity = solveForwardKinematics(wheelVelocity)
        dynamics.curvature = dynamics.chassisVelocity.angular / dynamics.chassisVelocity.linear
        if (dynamics.curvature.isNaN()) dynamics.curvature = 0.0
        dynamics.chassisAcceleration = solveForwardKinematics(wheelAcceleration)
        dynamics.dcurvature = (dynamics.chassisAcceleration.angular -
            dynamics.chassisAcceleration.linear * dynamics.curvature) /
            (dynamics.chassisVelocity.linear * dynamics.chassisVelocity.linear)
        if (dynamics.dcurvature.isNaN()) dynamics.dcurvature = 0.0
        dynamics.wheelVelocity = wheelVelocity
        dynamics.wheelAcceleration = wheelAcceleration
        solveInverseDynamics(dynamics)
        return dynamics
    }

    // Assumptions about dynamics: velocities and accelerations provided, curvature and dcurvature computed.
    public fun solveInverseDynamics(dynamics: DriveDynamics) {
        // Determine the necessary torques on the left and right wheels to produce the desired wheel accelerations.
        dynamics.wheelTorque.left = wheelRadius / 2.0 * (dynamics.chassisAcceleration.linear * mass -
                dynamics.chassisAcceleration.angular * moi / effectiveWheelbaseRadius -
                dynamics.chassisVelocity.angular * angularDrag / effectiveWheelbaseRadius)
        dynamics.wheelTorque.right = wheelRadius / 2.0 * (dynamics.chassisAcceleration.linear * mass +
                dynamics.chassisAcceleration.angular * moi / effectiveWheelbaseRadius +
                dynamics.chassisVelocity.angular * angularDrag / effectiveWheelbaseRadius)

        // Solve for input voltages.
        dynamics.voltage.left = leftTransmission.getVoltageForTorque(dynamics.wheelVelocity.left, dynamics
                .wheelTorque.left)
        dynamics.voltage.right = rightTransmission.getVoltageForTorque(dynamics.wheelVelocity.right, dynamics
                .wheelTorque.right)
    }

    @Suppress("ReturnCount")
    public fun getMaxAbsVelocity(curvature: Double, maxAbsVoltage: Double): Double {

        val leftSpeedAtMaxVoltage = leftTransmission.freeSpeedAtVoltage(maxAbsVoltage)
        val rightSpeedAtMaxVoltage = rightTransmission.freeSpeedAtVoltage(maxAbsVoltage)
        if (Utils.epsilonEquals(curvature, 0.0)) {
            return wheelRadius * Math.min(leftSpeedAtMaxVoltage, rightSpeedAtMaxVoltage)
        }
        if (curvature.isInfinite()) {
            // Turn in place.  Return value meaning becomes angular velocity.
            val wheelSpeed = Math.min(leftSpeedAtMaxVoltage, rightSpeedAtMaxVoltage)
            return Math.signum(curvature) * wheelRadius * wheelSpeed / effectiveWheelbaseRadius
        }

        val rightSpeedIfLeftMax = leftSpeedAtMaxVoltage * (effectiveWheelbaseRadius * curvature +
                1.0) / (1.0 - effectiveWheelbaseRadius * curvature)
        if (Math.abs(rightSpeedIfLeftMax) <= rightSpeedAtMaxVoltage + Constants.EPSILON) {
            // Left max is active constraint.
            return wheelRadius * (leftSpeedAtMaxVoltage + rightSpeedIfLeftMax) / 2.0
        }
        val leftSpeedIfRightMax = rightSpeedAtMaxVoltage * (1.0 - effectiveWheelbaseRadius *
                curvature) / (1.0 + effectiveWheelbaseRadius * curvature)
        // Right at max is active constraint.
        return wheelRadius * (rightSpeedAtMaxVoltage + leftSpeedIfRightMax) / 2.0
    }

    @Suppress("ComplexMethod", "NestedBlockDepth")
    public fun getMinMaxAcceleration(
        chassisVelocity: ChassisState,
        curvature: Double,
        maxAbsVoltage: Double
    ): MinMax {
        val result = MinMax()
        val wheelVelocities = solveInverseKinematics(chassisVelocity)
        result.min = Double.POSITIVE_INFINITY
        result.max = Double.NEGATIVE_INFINITY

        // Math:
        // (Tl + Tr) / r_w = m*a
        // (Tr - Tl) / r_w * r_wb - drag*w = i*(a * k + v^2 * dk)

        // 2 equations, 2 unknowns.
        // Solve for a and (Tl|Tr)

        val linearTerm = if (curvature.isInfinite()) 0.0 else mass * effectiveWheelbaseRadius
        val angularTerm = if (curvature.isInfinite()) moi else moi * curvature

        val dragTorque = chassisVelocity.angular * angularDrag

        val l1 = arrayOf<Boolean>(false, true)
        val l2 = arrayOf<Double>(1.0, -1.0)
        for (left in l1) {
            for (sign in l2) {
                val fixedTransmission = if (left) leftTransmission else rightTransmission
                val variableTransmission = if (left) rightTransmission else leftTransmission
                val fixedTorque = fixedTransmission.getTorqueForVoltage(
                    if (left) wheelVelocities.left else wheelVelocities.right,
                    sign * maxAbsVoltage
                )
                val variableTorque: Double
                if (left) {
                    variableTorque = ((-dragTorque) * mass * wheelRadius + fixedTorque * (linearTerm + angularTerm)) /
                        (linearTerm - angularTerm)
                } else {
                    variableTorque = ((+dragTorque) * mass * wheelRadius + fixedTorque * (linearTerm - angularTerm)) /
                        (linearTerm + angularTerm)
                }
                val variableVoltage = variableTransmission.getVoltageForTorque(
                    if (!left) wheelVelocities.left else wheelVelocities.right,
                    variableTorque
                )
                if (Math.abs(variableVoltage) <= maxAbsVoltage + Constants.EPSILON) {
                    val accel: Double
                    if (curvature.isInfinite()) {
                        accel = (if (left) -1.0 else 1.0) * (fixedTorque - variableTorque) * effectiveWheelbaseRadius /
                            (moi * wheelRadius) - dragTorque / moi
                    } else {
                        accel = (fixedTorque + variableTorque) / (mass * wheelRadius)
                    }
                    result.min = Math.min(result.min, accel)
                    result.min = Math.max(result.max, accel)
                }
            }
        }

        return result
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

    public data class MinMax(var min: Double = 0.0, var max: Double = 0.0)
}
