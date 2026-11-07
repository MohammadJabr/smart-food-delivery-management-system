
package najah.edu.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import najah.edu.domain.*;
import najah.edu.repository.OrderRepository;

class OrderServiceTest {

    private OrderRepository repository;
    private OrderService orderService;
    private Restaurant restaurant;
    private MenuItem pizza;

    @BeforeEach
    void setUp() {

        repository = new OrderRepository();
        orderService = new OrderService(repository);

        restaurant = new Restaurant("Pizza House", true);
        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(10.0);

        pizza = new MenuItem("Pizza", 10.0, true);
        restaurant.addMenuItem(pizza);
    }

    @Test
    void testCreateStandardOrder() {

        Order order = orderService.createOrder(
                restaurant, OrderType.STANDARD);

        assertNotNull(order);
        assertEquals(OrderType.STANDARD, order.getOrderType());
    }

    @Test
    void testCreateExpressOrder() {

        Order order = orderService.createOrder(
                restaurant, OrderType.EXPRESS);

        assertNotNull(order);
        assertEquals(OrderType.EXPRESS, order.getOrderType());
    }

    @Test
    void testCreateScheduledOrder() {

        Order order = orderService.createOrder(
                restaurant, OrderType.SCHEDULED);

        assertNotNull(order);
        assertEquals(OrderType.SCHEDULED, order.getOrderType());
    }

    @Test
    void testCannotCreateOrderWithNullType() {

        assertThrows(
                IllegalArgumentException.class,
                () -> orderService.createOrder(restaurant, null)
        );
    }

    @Test
    void testAddItemToOrder() {

        Order order = orderService.createOrder(
                restaurant, OrderType.STANDARD);

        orderService.addItem(order, pizza, 2);

        assertEquals(20.0, order.getTotal(), 0.001);
    }

    @Test
    void testSetDeliveryDetails() {

        Order order = orderService.createOrder(
                restaurant, OrderType.STANDARD);

        orderService.addItem(order, pizza, 2);

        orderService.setDeliveryDetails(
                order, "Nablus", 5.0);

        assertEquals("Nablus", order.getDeliveryAddress());
        assertEquals(5.0, order.getDeliveryDistance(), 0.001);
        assertEquals(10.0, order.getDeliveryFee(), 0.001);
    }

    @Test
    void testPlaceOrderAndSaveIt() {

        Order order = orderService.createOrder(
                restaurant, OrderType.STANDARD);

        orderService.addItem(order, pizza, 2);

        orderService.setDeliveryDetails(
                order, "Nablus", 5.0);

        orderService.placeOrder(order);

        assertEquals("PENDING", order.getStatus());
        assertEquals(1, repository.count());

        assertSame(order, orderService.getOrderByNumber(1));
    }

    @Test
    void testCannotPlaceEmptyOrder() {

        Order order = orderService.createOrder(
                restaurant, OrderType.STANDARD);

        assertThrows(
                IllegalArgumentException.class,
                () -> orderService.placeOrder(order)
        );

        assertEquals(0, repository.count());
    }

    @Test
    void testScheduledOrderRequiresDeliveryTime() {

        Order order = orderService.createOrder(
                restaurant, OrderType.SCHEDULED);

        orderService.addItem(order, pizza, 2);

        orderService.setDeliveryDetails(
                order, "Nablus", 5.0);

        assertThrows(
                IllegalStateException.class,
                () -> orderService.placeOrder(order)
        );

        assertEquals(0, repository.count());
    }

    @Test
    void testPlaceScheduledOrderWithFutureTime() {

        Order order = orderService.createOrder(
                restaurant, OrderType.SCHEDULED);

        orderService.addItem(order, pizza, 2);

        orderService.setDeliveryDetails(
                order, "Nablus", 5.0);

        LocalDateTime futureTime =
                LocalDateTime.now().plusDays(1);

        orderService.setScheduledDeliveryTime(
                order, futureTime);

        orderService.placeOrder(order);

        assertEquals("PENDING", order.getStatus());
        assertEquals(1, repository.count());
    }

    @Test
    void testCancelPendingOrder() {

        Order order = orderService.createOrder(
                restaurant, OrderType.STANDARD);

        orderService.addItem(order, pizza, 2);

        orderService.setDeliveryDetails(
                order, "Nablus", 5.0);

        orderService.placeOrder(order);

        orderService.cancelOrder(order);

        assertEquals("CANCELLED", order.getStatus());
    }

    @Test
    void testCannotCancelDraftOrder() {

        Order order = orderService.createOrder(
                restaurant, OrderType.STANDARD);

        assertThrows(
                IllegalStateException.class,
                () -> orderService.cancelOrder(order)
        );
    }

    @Test
    void testGetAllOrders() {

        Order order = orderService.createOrder(
                restaurant, OrderType.STANDARD);

        orderService.addItem(order, pizza, 2);

        orderService.setDeliveryDetails(
                order, "Nablus", 5.0);

        orderService.placeOrder(order);

        assertEquals(1, orderService.getAllOrders().size());
    }

    @Test
    void testInvalidOrderNumber() {

        assertThrows(
                IllegalArgumentException.class,
                () -> orderService.getOrderByNumber(1)
        );
    }
}
