package najah.edu.service;

import java.util.ArrayList;
import najah.edu.domain.User;

public class LoginService {

    private ArrayList<User> users;

    public LoginService(ArrayList<User> users) {

        if (users == null) {
            throw new IllegalArgumentException("Users list cannot be null");
        }

        this.users = users;
    }

    public User login(String name, String password) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        for (User user : users) {

            if (user.getName().equals(name)) {

                if (user.getpassword().equals(password)) {
                    return user;
                }

                throw new SecurityException("Invalid password");
            }
        }

        throw new SecurityException("Invalid username");
    }
}