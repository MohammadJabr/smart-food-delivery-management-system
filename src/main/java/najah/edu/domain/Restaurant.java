package najah.edu.domain;

public class Restaurant {

    private String name;
    private boolean open;

    public Restaurant(String name, boolean open) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Restaurant name cannot be empty"
            );
        }

        this.name = name;
        this.open = open;
    }

    public String getName() {
        return name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}