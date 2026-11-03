package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DriverAssignmentTest {

    @Test
    public void testAvailableDriverCanBeAssigned() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        Order order = new Order(restaurant);

        Driver driver = new Driver(
                "rami",
                Driver.Status.AVAILABLE
        );

        order.assignDriver(driver);

        assertEquals(driver, order.getAssignedDriver());
        assertEquals(Driver.Status.BUSY, driver.getStatus());
    }

    @Test
    public void testUnavailableDriverCannotBeAssigned() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        Order order = new Order(restaurant);

        Driver driver = new Driver(
                "rami",
                Driver.Status.BUSY
        );

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

    @Test
    public void testCannotAssignSecondDriverToSameOrder() {

        Restaurant restaurant = new Restaurant("Pizza House", true);
        Order order = new Order(restaurant);

        Driver firstDriver = new Driver(
                "Rami", Driver.Status.AVAILABLE
        );

        Driver secondDriver = new Driver(
                "Ahmad", Driver.Status.AVAILABLE
        );

        order.assignDriver(firstDriver);

        assertThrows(
                IllegalStateException.class,
                () -> order.assignDriver(secondDriver)
        );

        assertEquals(firstDriver, order.getAssignedDriver());
        assertEquals(Driver.Status.BUSY, firstDriver.getStatus());
        assertEquals(Driver.Status.AVAILABLE, secondDriver.getStatus());
    }
}