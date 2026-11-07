package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ScheduledOrderTest {

    private ScheduledOrder scheduledOrder;

    @BeforeEach
    public void setUp() {

        Restaurant restaurant =
                new Restaurant("Hulk Burger", true);

        scheduledOrder =
                new ScheduledOrder(restaurant);
    }

    @Test
    public void testScheduledOrderType() {

        assertEquals(
                OrderType.SCHEDULED,
                scheduledOrder.getOrderType()
        );
    }

    @Test
    public void testFutureDeliveryTimeIsAccepted() {

        LocalDateTime futureTime =
                LocalDateTime.now().plusHours(2);

        scheduledOrder.setScheduledDeliveryTime(futureTime);

        assertEquals(
                futureTime,
                scheduledOrder.getScheduledDeliveryTime()
        );
    }

    @Test
    public void testPastDeliveryTimeIsRejected() {

        LocalDateTime pastTime =
                LocalDateTime.now().minusHours(2);

        assertThrows(
                IllegalArgumentException.class,
                () -> scheduledOrder.setScheduledDeliveryTime(pastTime)
        );
    }

    @Test
    public void testNullDeliveryTimeIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> scheduledOrder.setScheduledDeliveryTime(null)
        );
    }
}