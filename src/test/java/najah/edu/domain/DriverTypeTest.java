package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class DriverTypeTest {

    @Test
    public void testCreateDifferentDriverTypes() {
        Driver bicycle = new BicycleDriver("Ahmad", Driver.Status.AVAILABLE);
        Driver motorcycle = new MotorcycleDriver("Ali", Driver.Status.AVAILABLE);
        Driver car = new CarDriver("Omar", Driver.Status.AVAILABLE);
        Driver electric = new ElectricVehicleDriver("Khaled", Driver.Status.AVAILABLE);

        assertEquals(DriverType.BICYCLE, bicycle.getDriverType());
        assertEquals(DriverType.MOTORCYCLE, motorcycle.getDriverType());
        assertEquals(DriverType.CAR, car.getDriverType());
        assertEquals(DriverType.ELECTRIC_VEHICLE, electric.getDriverType());
    }

    @Test
    public void testDifferentDriverTypesWorkPolymorphically() {
        List<Driver> drivers = Arrays.asList(
            new BicycleDriver("Ahmad", Driver.Status.AVAILABLE),
            new MotorcycleDriver("Ali", Driver.Status.AVAILABLE),
            new CarDriver("Omar", Driver.Status.AVAILABLE),
            new ElectricVehicleDriver("Khaled", Driver.Status.AVAILABLE)
        );

        assertEquals(4, drivers.size());

        for (Driver driver : drivers) {
            assertNotNull(driver);
            assertEquals(Driver.Status.AVAILABLE, driver.getStatus());
        }
    }
}