package najah.edu.domain;

public class Driver {

    private String name;
    private boolean available;

    public Driver(String name, boolean available) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Driver name cannot be empty"
            );
        }

        this.name = name;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}