package najah.edu.repository;

import najah.edu.domain.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class UserRepositoryTest {

    @Test
    void testSaveAndLoadUsers() throws IOException {

        File file = File.createTempFile("users_test", ".txt");
        file.deleteOnExit();

        UserRepository repository =
                new UserRepository(file.getAbsolutePath());

        
        User user = new User("sara", "s123456", "CUSTOMER");
        repository.addUser(user);

        assertEquals(1, repository.getUsers().size());

        UserRepository newRepository =
                new UserRepository(file.getAbsolutePath());

        ArrayList<User> users = newRepository.getUsers();

        
        assertEquals(1, users.size());
        assertEquals("sara", users.get(0).getName());
        assertEquals("s123456", users.get(0).getpassword());
        assertEquals("CUSTOMER", users.get(0).getRole());
    }
    
    @Test
    void testSaveAndLoadUserWithEmail() throws IOException {

        File file = File.createTempFile("users_email_test", ".txt");
        file.deleteOnExit();

        UserRepository repository =
                new UserRepository(file.getAbsolutePath());

        User user = new User(
                "sara",
                "s123456",
                "CUSTOMER",
                "sara@example.com"
        );

        repository.addUser(user);

        UserRepository newRepository =
                new UserRepository(file.getAbsolutePath());

        assertEquals(
                "sara@example.com",
                newRepository.getUsers().get(0).getEmail()
        );
    }

    @Test
    void testLoadLegacyUserWithoutEmail() throws IOException {

        File file = File.createTempFile("users_legacy_test", ".txt");
        file.deleteOnExit();

        try (java.io.BufferedWriter writer =
                new java.io.BufferedWriter(new java.io.FileWriter(file))) {

            writer.write("sara,s123456,CUSTOMER");
        }

        UserRepository repository =
                new UserRepository(file.getAbsolutePath());

        assertNull(repository.getUsers().get(0).getEmail());
    }
}