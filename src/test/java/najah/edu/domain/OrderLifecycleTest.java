package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderLifecycleTest {

    private Order order;

    @BeforeEach
    public void setUp() {

        Restaurant restaurant =
                new Restaurant("Hulk Burger", true);

        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(10.0);

        MenuItem burger =
                new MenuItem("Hulk Burger Meal", 20.0, true);

        restaurant.addMenuItem(burger);

        order = new Order(restaurant);

        order.addItem(burger, 1);
        order.setDeliveryAddress("Nablus");
        order.placeOrder();
    }

    @Test
    public void testCompleteOrderLifecycle() {

        assertEquals("PENDING", order.getStatus());

        order.confirmOrder();
        assertEquals("CONFIRMED", order.getStatus());

        order.startPreparing();
        assertEquals("PREPARING", order.getStatus());

        order.markReady();
        assertEquals("READY", order.getStatus());

        order.markOutForDelivery();
        assertEquals(
                "OUT_FOR_DELIVERY",
                order.getStatus()
        );

        order.markDelivered();
        assertEquals(
                "DELIVERED",
                order.getStatus()
        );
    }

    
    @Test
    public void testCannotStartPreparingFromPending() {

        assertThrows(
                IllegalStateException.class,
                () -> order.startPreparing()
        );

        assertEquals("PENDING", order.getStatus());
    }

    // Cannot go directly from PENDING to READY
    @Test
    public void testCannotMarkPendingOrderAsReady() {

        assertThrows(
                IllegalStateException.class,
                () -> order.markReady()
        );

        assertEquals("PENDING", order.getStatus());
    }

  
    @Test
    public void testCannotSkipPreparingState() {

        order.confirmOrder();

        assertThrows(
                IllegalStateException.class,
                () -> order.markReady()
        );

        assertEquals("CONFIRMED", order.getStatus());
    }

    // Cannot go directly from PREPARING to OUT_FOR_DELIVERY
    @Test
    public void testCannotSkipReadyState() {

        order.confirmOrder();
        order.startPreparing();

        assertThrows(
                IllegalStateException.class,
                () -> order.markOutForDelivery()
        );

        assertEquals("PREPARING", order.getStatus());
    }


    @Test
    public void testCannotDeliverOrderBeforeOutForDelivery() {

        order.confirmOrder();
        order.startPreparing();
        order.markReady();

        assertThrows(
                IllegalStateException.class,
                () -> order.markDelivered()
        );

        assertEquals("READY", order.getStatus());
    }

    // DELIVERED must be the final state
    @Test
    public void testDeliveredOrderIsFinal() {

        order.confirmOrder();
        order.startPreparing();
        order.markReady();
        order.markOutForDelivery();
        order.markDelivered();

        assertEquals("DELIVERED", order.getStatus());

        assertThrows(
                IllegalStateException.class,
                () -> order.confirmOrder()
        );

        assertThrows(
                IllegalStateException.class,
                () -> order.startPreparing()
        );

        assertThrows(
                IllegalStateException.class,
                () -> order.markReady()
        );

        assertThrows(
                IllegalStateException.class,
                () -> order.markOutForDelivery()
        );

        assertThrows(
                IllegalStateException.class,
                () -> order.markDelivered()
        );

        assertEquals("DELIVERED", order.getStatus());
    }
    @Test
    public void testDeliveredOrderCannotBeModified() {

        order.setDeliveryDistance(5.0);
        order.setPricedFinalBill(50.0);

        order.confirmOrder();
        order.startPreparing();
        order.markReady();
        order.markOutForDelivery();
        order.markDelivered();

        MenuItem extraItem =
                new MenuItem("Extra", 5.0, true);

        Driver newDriver =
                new Driver("Omar", Driver.Status.AVAILABLE);

        assertThrows(
                IllegalStateException.class,
                () -> order.addItem(extraItem, 1)
        );
        assertThrows(
                IllegalStateException.class,
                () -> order.setDeliveryAddress("Other address")
        );
        assertThrows(
                IllegalStateException.class,
                () -> order.setDeliveryDistance(10.0)
        );
        assertThrows(
                IllegalStateException.class,
                () -> order.setPricedFinalBill(60.0)
        );
        assertThrows(
                IllegalStateException.class,
                () -> order.assignDriver(newDriver)
        );
        assertThrows(
                IllegalStateException.class,
                () -> order.placeOrder()
        );

        assertEquals("DELIVERED", order.getStatus());
        assertEquals(1, order.getItems().size());
        assertEquals("Nablus", order.getDeliveryAddress());
        assertEquals(5.0, order.getDeliveryDistance(), 0.001);
        assertEquals(50.0, order.getFinalBill(), 0.001);
        assertTrue(newDriver.isAvailable());
    }

}