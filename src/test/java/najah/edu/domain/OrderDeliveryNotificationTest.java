package najah.edu.domain;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import najah.edu.notification.NotificationService;

import najah.edu.notification.EmailService;
import najah.edu.notification.EmailNotificationService;

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

    @Test
    public void testCustomerIsNotifiedWhenOrderGoesOutForDelivery() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(10.0);

        MenuItem pizza =
                new MenuItem("Pizza", 20.0, true);

        restaurant.addMenuItem(pizza);

        Order order = new Order(restaurant);

        order.addItem(pizza, 1);
        order.setDeliveryAddress("Nablus");

        order.placeOrder();
        order.confirmOrder();
        order.startPreparing();
        order.markReady();

        NotificationService notificationService =
                mock(NotificationService.class);

        order.setNotificationService(notificationService);

        order.markOutForDelivery();

        assertEquals("OUT_FOR_DELIVERY", order.getStatus());

        verify(notificationService).sendNotification(
                "Order status changed to OUT_FOR_DELIVERY"
        );
    }

    @Test
    public void testCustomerIsNotifiedWhenOrderIsDelivered() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(10.0);

        MenuItem pizza =
                new MenuItem("Pizza", 20.0, true);

        restaurant.addMenuItem(pizza);

        Order order = new Order(restaurant);

        order.addItem(pizza, 1);
        order.setDeliveryAddress("Nablus");

        order.placeOrder();
        order.confirmOrder();
        order.startPreparing();
        order.markReady();
        order.markOutForDelivery();

        NotificationService notificationService =
                mock(NotificationService.class);

        order.setNotificationService(notificationService);

        order.markDelivered();

        assertEquals("DELIVERED", order.getStatus());

        verify(notificationService).sendNotification(
                "Order status changed to DELIVERED"
        );
    }
    
    @Test
    public void testOrderEventSendsEmailToRegisteredCustomer() {

        User customer = new User(
                "Sara",
                "s123456",
                "CUSTOMER",
                "sara@example.com"
        );

        EmailService emailService = mock(EmailService.class);

        Order order = new Order(
                new Restaurant("Pizza House", true)
        );

        order.setNotificationService(
                new EmailNotificationService(customer, emailService)
        );

        Driver driver = new Driver(
                "Ahmad",
                Driver.Status.AVAILABLE
        );

        order.assignDriver(driver);

        verify(emailService).sendEmail(
                "sara@example.com",
                "Smart Food Delivery - Order Update",
                "A driver has been assigned to your order: Ahmad"
        );
    }
}