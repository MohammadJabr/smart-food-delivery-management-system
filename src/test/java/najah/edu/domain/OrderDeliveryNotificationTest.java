package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import najah.edu.notification.NotificationService;

public class OrderDeliveryNotificationTest {

    @Test
    public void testCustomerIsNotifiedWhenDriverAssigned() {

        Order order = new Order(
                new Restaurant("Pizza House", true)
        );

        NotificationService notificationService =
                mock(NotificationService.class);

        order.setNotificationService(notificationService);

        Driver driver = new Driver(
                "Rami",
                Driver.Status.AVAILABLE,
                2.0,
                10
        );

        order.assignDriver(driver);

        assertSame(driver, order.getAssignedDriver());

        verify(notificationService).sendNotification(
                "A driver has been assigned to your order: Rami"
        );
    }
}