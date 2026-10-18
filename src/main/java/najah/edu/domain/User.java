package najah.edu.domain;

public class User {

    private String name;
    private String password;
    private String role;

    public User(String name, String password, String role) {

        if (!isValidPassword(password)) {
            throw new IllegalArgumentException(
                "Password must contain at least 6 digits and 1 letter"
            );
        }

        this.name = name;
        this.password = password;
        this.role = role;
    }
    public boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        int numbers = 0;
        int letters = 0;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (Character.isDigit(c)) {
                numbers++;
            }

            if (Character.isLetter(c)) {
                letters++;
            }
        }

        if (numbers < 6 || letters < 1) {
            return false;
        }

        return true;
    }

    public String getName() {
        return name;
    }

    public String getpassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}