package najah.edu.notification;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import jakarta.mail.Message;
import jakarta.mail.Transport;

public class EmailServiceTest {

    @Test
    public void testSendEmailUsesTransport() {

        try (MockedStatic<Transport> transportMock =
                mockStatic(Transport.class)) {

            EmailService emailService =
                    new EmailService(
                            "sender@example.com",
                            "test-password"
                    );

            emailService.sendEmail(
                    "customer@example.com",
                    "Test Subject",
                    "Test Message"
            );

            transportMock.verify(
                    () -> Transport.send(any(Message.class))
            );
        }
    }

    @Test
    public void testEmptyCredentialsAreRejected() {

        assertThrows(
                IllegalStateException.class,
                () -> new EmailService("", "")
        );
    }

    @Test
    public void testNullCredentialsAreRejected() {

        assertThrows(
                IllegalStateException.class,
                () -> new EmailService(null, null)
        );
    }
}