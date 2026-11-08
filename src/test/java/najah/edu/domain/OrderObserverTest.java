
package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import najah.edu.notification.OrderObserver;

class OrderObserverTest {

    @Test
    void testObserverReceivesNotification() {

        Order order = new Order(
                new Restaurant("Pizza House", true)
        );

        OrderObserver observer = mock(OrderObserver.class);
        order.addObserver(observer);

        Driver driver = new Driver(
                "Rami",
                Driver.Status.AVAILABLE,
                2.0,
                10
        );

        order.assignDriver(driver);

        verify(observer).update(
                "A driver has been assigned to your order: Rami"
        );
    }

    @Test
    void testMultipleObserversReceiveNotification() {

        Order order = new Order(
                new Restaurant("Pizza House", true)
        );

        OrderObserver first = mock(OrderObserver.class);
        OrderObserver second = mock(OrderObserver.class);

        order.addObserver(first);
        order.addObserver(second);

        order.assignDriver(
                new Driver("Ahmad", Driver.Status.AVAILABLE)
        );

        verify(first).update(
                "A driver has been assigned to your order: Ahmad"
        );

        verify(second).update(
                "A driver has been assigned to your order: Ahmad"
        );
    }

    @Test
    void testDuplicateObserverIsNotRegisteredTwice() {

        Order order = new Order(
                new Restaurant("Pizza House", true)
        );

        OrderObserver observer = mock(OrderObserver.class);

        order.addObserver(observer);
        order.addObserver(observer);

        order.assignDriver(
                new Driver("Ahmad", Driver.Status.AVAILABLE)
        );

        verify(observer, times(1)).update(
                "A driver has been assigned to your order: Ahmad"
        );
    }

    @Test
    void testRemovedObserverDoesNotReceiveNotification() {

        Order order = new Order(
                new Restaurant("Pizza House", true)
        );

        OrderObserver observer = mock(OrderObserver.class);

        order.addObserver(observer);
        order.removeObserver(observer);

        order.assignDriver(
                new Driver("Ahmad", Driver.Status.AVAILABLE)
        );

        verifyNoInteractions(observer);
    }

    @Test
    void testNullObserverIsRejected() {

        Order order = new Order(
                new Restaurant("Pizza House", true)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> order.addObserver(null)
        );
    }
    

@Test
void testObserverReceivesOrderLifecycleNotifications() {

    Restaurant restaurant =
            new Restaurant("Pizza House", true);

    restaurant.setDeliveryZone("Nablus");
    restaurant.setMinimumOrderValue(10.0);

    MenuItem pizza =
            new MenuItem("Pizza", 20.0, true);

    restaurant.addMenuItem(pizza);

    Order order = new Order(restaurant);

    order.addItem(pizza, 1);
    order.setDeliveryAddress("Nablus");

    OrderObserver observer = mock(OrderObserver.class);
    order.addObserver(observer);

    order.placeOrder();
    order.confirmOrder();
    order.startPreparing();

    verify(observer).update(
            "Order status changed to PENDING"
    );

    verify(observer).update(
            "Order status changed to CONFIRMED"
    );

    verify(observer).update(
            "Order status changed to PREPARING"
    );
}

    @Test
    void testRestaurantReceivesOrderLifecycleNotifications() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);
        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(10.0);

        MenuItem pizza = new MenuItem("Pizza", 20.0, true);
        restaurant.addMenuItem(pizza);

        Order order = new Order(restaurant);
        order.addItem(pizza, 1);
        order.setDeliveryAddress("Nablus");

        order.placeOrder();
        order.confirmOrder();
        order.startPreparing();
        order.markReady();
        order.markOutForDelivery();
        order.markDelivered();

        assertTrue(restaurant.getNotifications().contains(
                "Order status changed to PENDING"));
        assertTrue(restaurant.getNotifications().contains(
                "Order status changed to DELIVERED"));
    }

    @Test
    void testAssignedDriverReceivesAssignmentAndLifecycleNotifications() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);
        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(10.0);

        MenuItem pizza = new MenuItem("Pizza", 20.0, true);
        restaurant.addMenuItem(pizza);

        Order order = new Order(restaurant);
        order.addItem(pizza, 1);
        order.setDeliveryAddress("Nablus");
        order.placeOrder();

        Driver driver =
                new Driver("Omar", Driver.Status.AVAILABLE);

        order.assignDriver(driver);
        order.confirmOrder();
        order.startPreparing();
        order.markReady();
        order.markOutForDelivery();
        order.markDelivered();

        assertTrue(driver.getNotifications().contains(
                "A driver has been assigned to your order: Omar"));
        assertTrue(driver.getNotifications().contains(
                "Order status changed to OUT_FOR_DELIVERY"));
        assertTrue(driver.getNotifications().contains(
                "Order status changed to DELIVERED"));
    }

}
