package najah.edu.notification;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import najah.edu.domain.User;

public class EmailNotificationServiceTest {

    @Test
    public void testNotificationIsSentToCustomerEmail() {

        EmailService emailService = mock(EmailService.class);

        EmailNotificationService notificationService =
                new EmailNotificationService(
                        "customer@example.com",
                        emailService
                );

        String notification = "Order status changed to PREPARING";

        notificationService.sendNotification(notification);

        verify(emailService).sendEmail(
                "customer@example.com",
                "Smart Food Delivery - Order Update",
                notification
        );
    }

    @Test
    public void testEmptyRecipientEmailIsRejected() {

        EmailService emailService = mock(EmailService.class);

        assertThrows(
                IllegalArgumentException.class,
                () -> new EmailNotificationService("", emailService)
        );
    }

    @Test
    public void testNullEmailServiceIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new EmailNotificationService(
                        "customer@example.com", null
                )
        );
    }
    
    @Test
    public void testNotificationUsesCustomerAccountEmail() {
    	

        User customer = new User(
                "Sara",
                "s123456",
                "CUSTOMER",
                "sara@example.com"
        );

        EmailService emailService = mock(EmailService.class);

        EmailNotificationService notificationService =
                new EmailNotificationService(customer, emailService);

        notificationService.sendNotification(
                "Your order has been delivered"
        );

        verify(emailService).sendEmail(
                "sara@example.com",
                "Smart Food Delivery - Order Update",
                "Your order has been delivered"
        );
    }
}