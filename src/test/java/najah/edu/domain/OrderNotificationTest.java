package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import najah.edu.notification.NotificationService;

public class OrderNotificationTest {

    @Test
    public void testNotificationSentWhenOrderStartsPreparing() {

        // Create restaurant
        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(10.0);

        // Create menu item
        MenuItem pizza =
                new MenuItem("Pizza", 20.0, true);

        restaurant.addMenuItem(pizza);

        // Create order
        Order order = new Order(restaurant);

        order.addItem(pizza, 1);
        order.setDeliveryAddress("Nablus");

        // Create Mock NotificationService
        NotificationService notificationService =
                mock(NotificationService.class);

        // Give the mock service to the order
        order.setNotificationService(notificationService);

        // Make order PENDING
        order.placeOrder();

        // Change status to PREPARING
        order.startPreparing();

        // Check status
        assertEquals("PREPARING", order.getStatus());

        // Verify that notification was sent
        verify(notificationService).sendNotification(
                "Order status changed to PREPARING"
        );
    }
}