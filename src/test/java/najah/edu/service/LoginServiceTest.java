package najah.edu.service;

import java.util.ArrayList;
import najah.edu.domain.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {

    @Test
    void testLogin() {
        ArrayList<User> users = new ArrayList<User>();
        users.add(new User("farah", "f123456", "CUSTOMER"));
        users.add(new User("mohammed", "m567890", "MANAGER"));

        LoginService service = new LoginService(users);

        User customer = service.login("farah", "f123456");
        assertEquals("CUSTOMER", customer.getRole());

        User manager = service.login("mohammed", "m567890");
        assertEquals("MANAGER", manager.getRole());

        assertThrows(SecurityException.class, new Executable() {
            @Override
            public void execute() {
                service.login("Wrong", "f123456");
            }
        });

        assertThrows(SecurityException.class, new Executable() {
            @Override
            public void execute() {
                service.login("farah", "f999999");
            }
        });
    }
    @Test
    void testInvalidPasswordFormat() {

        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() {
                new User("farah", "1234", "CUSTOMER");
            }
        });
    }
}