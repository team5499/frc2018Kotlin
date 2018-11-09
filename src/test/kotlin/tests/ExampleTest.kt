package tests

import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.api.mockito.PowerMockito
import org.mockito.Mockito

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX

@RunWith(PowerMockRunner::class)
@PrepareForTest(TalonSRX::class)
public class ExampleTest {
    @Test
    fun test() {
        var fakeTalon = PowerMockito.mock(TalonSRX::class.java)
        PowerMockito.whenNew(TalonSRX::class.java).withArguments(1).thenReturn(fakeTalon)

        var talon = TalonSRX(1)
        talon.set(ControlMode.PercentOutput, 0.5)

        Mockito.verify(fakeTalon).set(ControlMode.PercentOutput, 0.5)
    }
}
