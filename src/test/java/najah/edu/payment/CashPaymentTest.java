package najah.edu.payment;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import najah.edu.domain.MenuItem;
import najah.edu.domain.Order;
import najah.edu.domain.Restaurant;

public class CashPaymentTest {

    private CashPayment cashPayment;
    private Order order;

    @BeforeEach
    public void setUp() {

        cashPayment = new CashPayment();

        Restaurant restaurant =
                new Restaurant("Hulk Burger", true);

        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(10.0);

        MenuItem burger =
                new MenuItem("Burger", 20.0, true);

        restaurant.addMenuItem(burger);

        order = new Order(restaurant);

        // 2 burgers = 40
        order.addItem(burger, 2);

        // Delivery: 5 km × 2 = 10
        order.setDeliveryDistance(5.0);

        // Final Bill = 40 + 10 = 50
    }

    @Test
    public void testPaymentInitiallyNotPaid() {

        assertFalse(cashPayment.isPaid());
    }

    @Test
    public void testValidCashPayment() {

        cashPayment.processPayment(order, 50.0);

        assertTrue(cashPayment.isPaid());
    }

    @Test
    public void testZeroPaymentIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> cashPayment.processPayment(order, 0)
        );

        assertFalse(cashPayment.isPaid());
    }

    @Test
    public void testNegativePaymentIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> cashPayment.processPayment(order, -10.0)
        );

        assertFalse(cashPayment.isPaid());
    }

    @Test
    public void testPaymentLessThanFinalBillIsRejected() {

        // Required = 50, customer pays 40
        assertThrows(
                IllegalArgumentException.class,
                () -> cashPayment.processPayment(order, 40.0)
        );

        assertFalse(cashPayment.isPaid());
    }

    @Test
    public void testPaymentGreaterThanFinalBillIsRejected() {

        // Required = 50, customer pays 70
        assertThrows(
                IllegalArgumentException.class,
                () -> cashPayment.processPayment(order, 70.0)
        );

        assertFalse(cashPayment.isPaid());
    }

    @Test
    public void testNullOrderIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> cashPayment.processPayment(null, 50.0)
        );

        assertFalse(cashPayment.isPaid());
    }
}