package najah.edu.payment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        order.addItem(burger, 2);
        order.setDeliveryDistance(5.0);
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
        assertThrows(
                IllegalArgumentException.class,
                () -> cashPayment.processPayment(order, 40.0)
        );

        assertFalse(cashPayment.isPaid());
    }

    @Test
    public void testPaymentGreaterThanFinalBillIsRejected() {
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

    @Test
    public void testCashPaymentUsingMockOrder() {

        Order mockOrder = mock(Order.class);

        when(mockOrder.getFinalBill()).thenReturn(50.0);

        CashPayment payment = new CashPayment();

        payment.processPayment(mockOrder, 50.0);

        assertTrue(payment.isPaid());

        verify(mockOrder).getFinalBill();
    }
}