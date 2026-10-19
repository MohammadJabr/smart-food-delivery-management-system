package najah.edu.domain;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Restaurant {

    private String name;
    private boolean open;
    private final List<MenuItem> menuItems;


    public Restaurant(String name, boolean open) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Restaurant name cannot be empty"
            );
        }

        this.name = name;
        this.open = open;
        this.menuItems = new ArrayList<>();
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
    
    public void addMenuItem(MenuItem item) {
        if (item == null) {
            throw new IllegalArgumentException(
                    "Menu item cannot be null"
            );
        }

        menuItems.add(item);
    }

    public List<MenuItem> getMenuItems() {
        return Collections.unmodifiableList(
                new ArrayList<>(menuItems)
        );
    }
}
