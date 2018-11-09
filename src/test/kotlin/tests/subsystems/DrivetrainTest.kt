package tests.subsystems

import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.api.mockito.PowerMockito
import org.mockito.Mockito

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.sensors.PigeonIMU

import frc.team5499.frc2018Kotlin.Constants
import frc.team5499.frc2018Kotlin.subsystems.Drivetrain
import frc.team5499.frc2018Kotlin.utils.Utils

@RunWith(PowerMockRunner::class)
@PrepareForTest(Drivetrain::class, TalonSRX::class)
public class DrivetrainTest {
    val fakeLeftMasterTalon = PowerMockito.mock(TalonSRX::class.java)
    val fakeLeftSlaveTalon = PowerMockito.mock(TalonSRX::class.java)
    val fakeRightMasterTalon = PowerMockito.mock(TalonSRX::class.java)
    val fakeRightSlaveTalon = PowerMockito.mock(TalonSRX::class.java)
    val fakeGyro = PowerMockito.mock(PigeonIMU::class.java)

    fun setup_mocks() {
        PowerMockito.whenNew(TalonSRX::class.java)
                .withArguments(Constants.Talons.LEFT_MASTER_PORT)
                .thenReturn(fakeLeftMasterTalon)
        PowerMockito.whenNew(TalonSRX::class.java)
                .withArguments(Constants.Talons.LEFT_SLAVE_PORT)
                .thenReturn(fakeLeftSlaveTalon)
        PowerMockito.whenNew(TalonSRX::class.java)
                .withArguments(Constants.Talons.RIGHT_MASTER_PORT)
                    .thenReturn(fakeRightMasterTalon)
        PowerMockito.whenNew(TalonSRX::class.java)
                .withArguments(Constants.Talons.RIGHT_SLAVE_PORT)
                .thenReturn(fakeRightSlaveTalon)
        PowerMockito.whenNew(PigeonIMU::class.java)
                .withArguments(Constants.Gyro.GYRO_PORT_NUMBER)
                .thenReturn(fakeGyro)
    }

    @Test
    fun test_setVelocity() {
        setup_mocks()

        // Test regular behavior
        Drivetrain.setVelocity(-10.0, 10.0)

        var expectedVelocity = Utils.inchesPerSecondToEncoderTicksPer100Ms(10.0)

        Mockito.verify(fakeLeftMasterTalon).set(ControlMode.Velocity, -expectedVelocity)
        Mockito.verify(fakeRightMasterTalon).set(ControlMode.Velocity, expectedVelocity)

        // Test MAX_VELOCITY_SETPOINT is used for limiting
        Drivetrain.setVelocity(Constants.MAX_VELOCITY_SETPOINT * -2.0, Constants.MAX_VELOCITY_SETPOINT * 2.0)

        expectedVelocity = Utils.inchesPerSecondToEncoderTicksPer100Ms(Constants.MAX_VELOCITY_SETPOINT)

        Mockito.verify(fakeLeftMasterTalon).set(ControlMode.Velocity, -expectedVelocity)
        Mockito.verify(fakeRightMasterTalon).set(ControlMode.Velocity, expectedVelocity)
    }
}
