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
import najah.edu.domain.BicycleDriver;
import najah.edu.domain.MotorcycleDriver;
import najah.edu.domain.CarDriver;
import najah.edu.domain.ElectricVehicleDriver;
import najah.edu.domain.MenuItem;

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


private Order createOrder(int quantity, double distance) {

    Restaurant restaurant =
            new Restaurant("Pizza House", true);

    MenuItem pizza =
            new MenuItem("Pizza", 10.0, true);

    restaurant.addMenuItem(pizza);

    Order order = new Order(restaurant);

    if (quantity > 0) {
        order.addItem(pizza, quantity);
    }

    order.setDeliveryDistance(distance);

    return order;
}

@Test
public void testBicycleCannotDeliverLongDistance() {

    Order order = createOrder(2, 8.0);

    Driver bicycle = new BicycleDriver(
            "Ahmad", Driver.Status.AVAILABLE, 1.0, 5);

    DriverAssignmentService service =
            new DriverAssignmentService(new NearestDriverStrategy());

    assertThrows(
            IllegalStateException.class,
            () -> service.assignDriver(order, Arrays.asList(bicycle))
    );

    assertTrue(bicycle.isAvailable());
    assertNull(order.getAssignedDriver());
}

@Test
public void testMotorcycleCanHandleMediumDistance() {

    Order order = createOrder(2, 8.0);

    Driver bicycle = new BicycleDriver(
            "Ahmad", Driver.Status.AVAILABLE, 1.0, 5);

    Driver motorcycle = new MotorcycleDriver(
            "Omar", Driver.Status.AVAILABLE, 3.0, 10);

    DriverAssignmentService service =
            new DriverAssignmentService(new NearestDriverStrategy());

    Driver selected = service.assignDriver(
            order, Arrays.asList(bicycle, motorcycle));

    assertSame(motorcycle, selected);
    assertEquals(Driver.Status.BUSY, motorcycle.getStatus());
    assertTrue(bicycle.isAvailable());
}

@Test
public void testElectricVehicleBatteryRangeIsEnforced() {

    Order order = createOrder(2, 12.0);

    Driver electricVehicle = new ElectricVehicleDriver(
            "Sami", Driver.Status.AVAILABLE,
            1.0, 5, 10.0);

    DriverAssignmentService service =
            new DriverAssignmentService(new NearestDriverStrategy());

    assertThrows(
            IllegalStateException.class,
            () -> service.assignDriver(
                    order, Arrays.asList(electricVehicle))
    );

    assertTrue(electricVehicle.isAvailable());
}

@Test
public void testCarCannotHandleMoreThanTwentyItems() {

    Order order = createOrder(21, 3.0);

    Driver car = new CarDriver(
            "Khaled", Driver.Status.AVAILABLE, 2.0, 8);

    DriverAssignmentService service =
            new DriverAssignmentService(new NearestDriverStrategy());

    assertThrows(
            IllegalStateException.class,
            () -> service.assignDriver(order, Arrays.asList(car))
    );

    assertTrue(car.isAvailable());
}

@Test
public void testCarCanHandleTwentyItems() {

    Order order = createOrder(20, 3.0);

    Driver car = new CarDriver(
            "Khaled", Driver.Status.AVAILABLE, 2.0, 8);

    DriverAssignmentService service =
            new DriverAssignmentService(new NearestDriverStrategy());

    Driver selected =
            service.assignDriver(order, Arrays.asList(car));

    assertSame(car, selected);
    assertEquals(Driver.Status.BUSY, car.getStatus());
}

}