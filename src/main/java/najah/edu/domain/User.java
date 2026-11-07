package najah.edu.domain;

public class User {

    private String name;
    private String password;
    private String role;
    private String email;

    public User(String name, String password, String role) {
        this(name, password, role, null, false);
    }

    public User(String name, String password, String role, String email) {
        this(name, password, role, email, true);
    }

    private User(
            String name,
            String password,
            String role,
            String email,
            boolean emailRequired) {

        if (!isValidPassword(password)) {
            throw new IllegalArgumentException(
                    "Password must contain at least 6 digits and 1 letter"
            );
        }

        if (emailRequired && !isValidEmail(email)) {
            throw new IllegalArgumentException(
                    "Invalid email address"
            );
        }

        if (email != null && !email.trim().isEmpty()
                && !isValidEmail(email)) {
            throw new IllegalArgumentException(
                    "Invalid email address"
            );
        }

        this.name = name;
        this.password = password;
        this.role = role;
        this.email = (email == null || email.trim().isEmpty())
                ? null : email.trim();
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

        return numbers >= 6 && letters >= 1;
    }

    private boolean isValidEmail(String email) {

        return email != null && email.trim().matches(
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        );
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

    public String getEmail() {
        return email;
    }
}