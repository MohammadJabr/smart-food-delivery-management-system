package najah.edu.domain;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import najah.edu.notification.OrderObserver;

public class Restaurant implements OrderObserver {

    private String name;
    private boolean open;
    private String deliveryZone;
    private double minimumOrderValue;
    private final List<MenuItem> menuItems;
    private final List<String> notifications = new ArrayList<>();

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

    public String getDeliveryZone() {
        return deliveryZone;
    }

    public void setDeliveryZone(String deliveryZone) {

        if (deliveryZone == null || deliveryZone.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Delivery zone cannot be empty"
            );
        }

        this.deliveryZone = deliveryZone.trim();
    }

    public double getMinimumOrderValue() {
        return minimumOrderValue;
    }

    public void setMinimumOrderValue(double minimumOrderValue) {

        if (minimumOrderValue < 0) {
            throw new IllegalArgumentException(
                    "Minimum order value cannot be negative"
            );
        }

        this.minimumOrderValue = minimumOrderValue;
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
    /**
     * Returns the notifications received for this restaurant's orders.
     *
     * @return an unmodifiable copy of the notification messages
     */
    public List<String> getNotifications() {
        return Collections.unmodifiableList(
                new ArrayList<>(notifications));
    }

    @Override
    public void update(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Notification message cannot be empty");
        }

        notifications.add(message);
        System.out.println(
                "Restaurant " + name + " notified: " + message);
    }

}