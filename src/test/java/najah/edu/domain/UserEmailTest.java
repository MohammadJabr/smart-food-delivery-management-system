package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UserEmailTest {

    @Test
    public void testUserStoresEmail() {
        User user = new User(
                "Sara",
                "s123456",
                "CUSTOMER",
                "sara@example.com"
        );

        assertEquals("sara@example.com", user.getEmail());
    }

    @Test
    public void testEmailIsTrimmed() {
        User user = new User(
                "Sara",
                "s123456",
                "CUSTOMER",
                " sara@example.com "
        );

        assertEquals("sara@example.com", user.getEmail());
    }

    @Test
    public void testInvalidEmailIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new User(
                        "Sara",
                        "s123456",
                        "CUSTOMER",
                        "invalid-email"
                )
        );
    }

    @Test
    public void testOldConstructorRemainsCompatible() {
        User user = new User(
                "Sara",
                "s123456",
                "CUSTOMER"
        );

        assertNull(user.getEmail());
    }
}