package najah.edu.notification;
import najah.edu.domain.User;

public class EmailNotificationService implements NotificationService, OrderObserver {

    private static final String SUBJECT =
            "Smart Food Delivery - Order Update";

    private final String recipientEmail;
    private final EmailService emailService;

    public EmailNotificationService(String recipientEmail) {
        this(recipientEmail, new EmailService());
    }

    public EmailNotificationService(
            String recipientEmail,
            EmailService emailService) {

        if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Recipient email cannot be empty"
            );
        }

        if (emailService == null) {
            throw new IllegalArgumentException(
                    "EmailService cannot be null"
            );
        }

        this.recipientEmail = recipientEmail.trim();
        this.emailService = emailService;
    }

    public EmailNotificationService(User customer) {
        this(requireCustomerEmail(customer), new EmailService());
    }

    public EmailNotificationService(
            User customer,
            EmailService emailService) {

        this(requireCustomerEmail(customer), emailService);
    }

    private static String requireCustomerEmail(User customer) {

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer cannot be null"
            );
        }

        if (customer.getEmail() == null
                || customer.getEmail().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Customer email is required"
            );
        }

        return customer.getEmail().trim();
    }

    @Override
    public void sendNotification(String message) {

        emailService.sendEmail(
                recipientEmail,
                SUBJECT,
                message
        );
    }

    @Override
    public void update(String message) {
        sendNotification(message);
    }

}
