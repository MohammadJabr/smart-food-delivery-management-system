
package najah.edu.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import najah.edu.domain.MenuItem;
import najah.edu.domain.Order;
import najah.edu.domain.Restaurant;
import static org.mockito.Mockito.*;

import najah.edu.payment.PaymentMethod;

class PaymentServiceTest {

    private PaymentService paymentService;
    private Order order;

    @BeforeEach
    void setUp() {

        paymentService = new PaymentService();

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(10.0);

        MenuItem pizza =
                new MenuItem("Pizza", 20.0, true);

        restaurant.addMenuItem(pizza);

        order = new Order(restaurant);

        order.addItem(pizza, 2);
        order.setDeliveryAddress("Nablus");
        order.setDeliveryDistance(5.0);
        order.placeOrder();
    }

    @Test
    void testSuccessfulCashPayment() {

        paymentService.processCashPayment(order, 50.0);

        assertTrue(paymentService.isPaid(order));
        assertEquals("PAID",
                paymentService.getPaymentStatus(order));
    }

    @Test
    void testUnderpaymentIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.processCashPayment(order, 30.0)
        );

        assertFalse(paymentService.isPaid(order));
    }

    @Test
    void testOverpaymentIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.processCashPayment(order, 60.0)
        );

        assertFalse(paymentService.isPaid(order));
    }

    @Test
    void testZeroPaymentIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.processCashPayment(order, 0)
        );

        assertFalse(paymentService.isPaid(order));
    }

    @Test
    void testCannotPayTwice() {

        paymentService.processCashPayment(order, 50.0);

        assertThrows(
                IllegalStateException.class,
                () -> paymentService.processCashPayment(order, 50.0)
        );
    }

    @Test
    void testNullOrderIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.processCashPayment(null, 50.0)
        );
    }
    
    @Test
    void testUsesInjectedPaymentMethod() {

        PaymentMethod mockPayment = mock(PaymentMethod.class);

        when(mockPayment.isPaid()).thenReturn(false);

        PaymentService service =
                new PaymentService(() -> mockPayment);

        service.processCashPayment(order, 50.0);

        verify(mockPayment).processPayment(order, 50.0);
    }

    @Test
    void testRejectsPaymentWhenMockReportsAlreadyPaid() {

        PaymentMethod mockPayment = mock(PaymentMethod.class);

        when(mockPayment.isPaid()).thenReturn(false, true);

        PaymentService service =
                new PaymentService(() -> mockPayment);

        service.processCashPayment(order, 50.0);

        assertThrows(
                IllegalStateException.class,
                () -> service.processCashPayment(order, 50.0)
        );

        verify(mockPayment, times(1))
                .processPayment(order, 50.0);
    }
}
