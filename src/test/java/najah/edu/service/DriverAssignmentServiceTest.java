package najah.edu.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import najah.edu.domain.Driver;
import najah.edu.domain.Order;
import najah.edu.domain.Restaurant;
import najah.edu.strategy.NearestDriverStrategy;
import najah.edu.strategy.FastestDriverStrategy;

public class DriverAssignmentServiceTest {

    @Test
    public void testAssignsNearestAvailableDriver() {

        Order order = new Order(
                new Restaurant("Pizza House", true)
        );

        Driver rami = new Driver(
                "Rami", Driver.Status.AVAILABLE, 5.0, 20
        );

        Driver ahmad = new Driver(
                "Ahmad", Driver.Status.AVAILABLE, 2.0, 15
        );

        List<Driver> drivers = Arrays.asList(rami, ahmad);

        DriverAssignmentService service =
                new DriverAssignmentService(
                        new NearestDriverStrategy()
                );

        Driver selected = service.assignDriver(order, drivers);

        assertSame(ahmad, selected);
        assertSame(ahmad, order.getAssignedDriver());
        assertEquals(Driver.Status.BUSY, ahmad.getStatus());
    }

    @Test
    public void testAssignsFastestAvailableDriver() {

        Order order = new Order(
                new Restaurant("Pizza House", true)
        );

        Driver rami = new Driver(
                "Rami", Driver.Status.AVAILABLE, 2.0, 15
        );

        Driver ahmad = new Driver(
                "Ahmad", Driver.Status.AVAILABLE, 5.0, 7
        );

        List<Driver> drivers = Arrays.asList(rami, ahmad);

        DriverAssignmentService service =
                new DriverAssignmentService(
                        new FastestDriverStrategy()
                );

        Driver selected = service.assignDriver(order, drivers);

        assertSame(ahmad, selected);
        assertSame(ahmad, order.getAssignedDriver());
        assertEquals(Driver.Status.BUSY, ahmad.getStatus());
    }

    @Test
    public void testRejectsAssignmentWhenNoDriverIsAvailable() {

        Order order = new Order(
                new Restaurant("Pizza House", true)
        );

        Driver rami = new Driver(
                "Rami", Driver.Status.BUSY, 2.0, 10
        );

        Driver ahmad = new Driver(
                "Ahmad", Driver.Status.BUSY, 3.0, 15
        );

        List<Driver> drivers = Arrays.asList(rami, ahmad);

        DriverAssignmentService service =
                new DriverAssignmentService(
                        new NearestDriverStrategy()
                );

        assertThrows(
                IllegalStateException.class,
                () -> service.assignDriver(order, drivers)
        );

        assertNull(order.getAssignedDriver());
    }

    @Test
    public void testRejectsNullOrder() {

        Driver driver = new Driver(
                "Rami", Driver.Status.AVAILABLE, 2.0, 10
        );

        DriverAssignmentService service =
                new DriverAssignmentService(
                        new NearestDriverStrategy()
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.assignDriver(
                        null,
                        Arrays.asList(driver)
                )
        );
    }

    @Test
    public void testRejectsNullStrategy() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new DriverAssignmentService(null)
        );
    }
}