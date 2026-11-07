package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import najah.edu.service.DateTimeService;

public class ScheduledOrderTest {

    private ScheduledOrder scheduledOrder;
    private DateTimeService dateTimeService;

    @BeforeEach
    public void setUp() {

        Restaurant restaurant =
                new Restaurant("Hulk Burger", true);

        dateTimeService = mock(DateTimeService.class);

        scheduledOrder =
                new ScheduledOrder(restaurant, dateTimeService);
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

        LocalDateTime currentTime =
                LocalDateTime.of(2026, 11, 7, 10, 0);

        LocalDateTime futureTime =
                LocalDateTime.of(2026, 11, 7, 12, 0);

        when(dateTimeService.getCurrentTime())
                .thenReturn(currentTime);

        scheduledOrder.setScheduledDeliveryTime(futureTime);

        assertEquals(
                futureTime,
                scheduledOrder.getScheduledDeliveryTime()
        );

        verify(dateTimeService).getCurrentTime();
    }

    @Test
    public void testPastDeliveryTimeIsRejected() {

        LocalDateTime currentTime =
                LocalDateTime.of(2026, 11, 7, 10, 0);

        LocalDateTime pastTime =
                LocalDateTime.of(2026, 11, 7, 8, 0);

        when(dateTimeService.getCurrentTime())
                .thenReturn(currentTime);

        assertThrows(
                IllegalArgumentException.class,
                () -> scheduledOrder.setScheduledDeliveryTime(pastTime)
        );

        verify(dateTimeService).getCurrentTime();
    }

    @Test
    public void testNullDeliveryTimeIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> scheduledOrder.setScheduledDeliveryTime(null)
        );

        verifyNoInteractions(dateTimeService);
    }
}