package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class OrderTypeTest {

    @Test
    public void testCreateDifferentOrderTypes() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        Order standardOrder =
                new StandardOrder(restaurant);

        Order expressOrder =
                new ExpressOrder(restaurant);

        Order scheduledOrder =
                new ScheduledOrder(restaurant);

        assertEquals(
                OrderType.STANDARD,
                standardOrder.getOrderType()
        );

        assertEquals(
                OrderType.EXPRESS,
                expressOrder.getOrderType()
        );

        assertEquals(
                OrderType.SCHEDULED,
                scheduledOrder.getOrderType()
        );
    }

    @Test
    public void testAllOrderTypesCanBeHandledPolymorphically() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        List<Order> orders = Arrays.asList(
                new StandardOrder(restaurant),
                new ExpressOrder(restaurant),
                new ScheduledOrder(restaurant)
        );

        assertEquals(3, orders.size());

        for (Order order : orders) {
            assertNotNull(order);
            assertEquals("DRAFT", order.getStatus());
        }
    }
}