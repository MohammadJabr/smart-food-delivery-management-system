package najah.edu.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import najah.edu.domain.User;

class LoginServiceTest {

    @Test
    void testLoginCorrect() {
    User user = new User("farah", "1234");
     LoginService service = new LoginService(user);
     boolean result = service.login("farah", "1234");
      assertTrue(result);
    }
    @Test
    void testLoginIncorrect() {
     User user = new User("farah", "1234");
     LoginService service = new LoginService(user);
     boolean result = service.login("farah", "9999");
       assertFalse(result);
    }
}