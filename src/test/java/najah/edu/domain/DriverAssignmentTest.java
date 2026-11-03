package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DriverAssignmentTest {

    @Test
    public void testAvailableDriverCanBeAssigned() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        Order order = new Order(restaurant);

        Driver driver =
                new Driver("rami", true);

        order.assignDriver(driver);

        assertEquals(driver, order.getAssignedDriver());
        assertFalse(driver.isAvailable());
    }

    @Test
    public void testUnavailableDriverCannotBeAssigned() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        Order order = new Order(restaurant);

        Driver driver =
                new Driver("rami", false);

        assertThrows(
                IllegalArgumentException.class,
                () -> order.assignDriver(driver)
        );
    }

    @Test
    public void testNullDriverCannotBeAssigned() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        Order order = new Order(restaurant);

        assertThrows(
                IllegalArgumentException.class,
                () -> order.assignDriver(null)
        );
    }
}