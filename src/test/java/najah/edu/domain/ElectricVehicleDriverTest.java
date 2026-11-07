package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ElectricVehicleDriverTest {

    @Test
    public void testElectricVehicleBatteryRangeRules() {

        ElectricVehicleDriver driver =
                new ElectricVehicleDriver(
                        "Khaled",
                        Driver.Status.AVAILABLE,
                        50.0
                );

        assertTrue(driver.canDeliverDistance(30.0));
        assertTrue(driver.canDeliverDistance(50.0));
        assertFalse(driver.canDeliverDistance(60.0));

        assertThrows(
                IllegalArgumentException.class,
                () -> driver.canDeliverDistance(0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> driver.canDeliverDistance(-5.0)
        );
    }
}