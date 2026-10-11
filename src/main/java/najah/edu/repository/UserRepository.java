package najah.edu.repository;

import java.util.ArrayList;
import java.io.*;

import najah.edu.domain.User;

public class UserRepository {

    private ArrayList<User> users = new ArrayList<User>();
    private String fileName;

    public UserRepository(String fileName) {
        this.fileName = fileName;
        loadUsers();
    }

    public void loadUsers() {

        users.clear();

        try (BufferedReader reader =
                new BufferedReader(new FileReader(fileName))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length == 3) {

                    User user = new User(
                        data[0],
                        data[1],
                        data[2]
                    );

                    users.add(user);
                }
            }

        } catch (FileNotFoundException e) {
            throw new IllegalStateException(
                "Users file not found", e
            );

        } catch (IOException e) {
            throw new IllegalStateException(
                "Error reading users file", e
            );
        }
    }

    public void addUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        for (User u : users) {
            if (u.getName().equals(user.getName())) {
                throw new IllegalArgumentException(
                    "Username already exists"
                );
            }
        }

        try (BufferedWriter writer =
                new BufferedWriter(new FileWriter(fileName, true))) {

            writer.write(
                user.getName() + "," +
                user.getpassword() + "," +
                user.getRole()
            );

            writer.newLine();

            users.add(user);

        } catch (IOException e) {
            throw new IllegalStateException(
                "Error saving user", e
            );
        }
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}