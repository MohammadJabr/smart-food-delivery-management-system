package najah.edu.domain;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private Restaurant restaurant;
    private List<OrderItem> items;
    private String status;
    private String deliveryAddress;
    private Driver assignedDriver;

    public Order(Restaurant restaurant) {

        if (restaurant == null) {
            throw new IllegalArgumentException(
                    "Restaurant cannot be null"
            );
        }

        if (!restaurant.isOpen()) {
            throw new IllegalArgumentException(
                    "Cannot place an order from a closed restaurant"
            );
        }

        this.restaurant = restaurant;
        this.items = new ArrayList<>();
        this.status = "DRAFT";
        this.assignedDriver = null;
    }

    public void addItem(MenuItem menuItem, int quantity) {

        if (menuItem == null) {
            throw new IllegalArgumentException(
                    "Menu item cannot be null"
            );
        }

        if (!restaurant.getMenuItems().contains(menuItem)) {
            throw new IllegalArgumentException(
                    "Menu item does not belong to this restaurant"
            );
        }

        if (!menuItem.isAvailable()) {
            throw new IllegalArgumentException(
                    "Cannot add unavailable menu item to the order"
            );
        }

        OrderItem orderItem =
                new OrderItem(menuItem, quantity);

        items.add(orderItem);
    }

    public double getTotal() {

        double total = 0;

        for (OrderItem item : items) {
            total += item.getSubtotal();
        }

        return total;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public String getStatus() {
        return status;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {

        if (deliveryAddress == null ||
                deliveryAddress.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Delivery address cannot be empty"
            );
        }

        this.deliveryAddress = deliveryAddress.trim();
    }

    public Driver getAssignedDriver() {
        return assignedDriver;
    }

    public void assignDriver(Driver driver) {

        if (driver == null) {
            throw new IllegalArgumentException(
                    "Driver cannot be null"
            );
        }

        if (!driver.isAvailable()) {
            throw new IllegalArgumentException(
                    "Driver is not available"
            );
        }

        this.assignedDriver = driver;
        driver.setAvailable(false);
    }

    public void placeOrder() {

        if (items.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot place an empty order"
            );
        }

        if (deliveryAddress == null) {
            throw new IllegalArgumentException(
                    "Delivery address is required"
            );
        }

        if (restaurant.getDeliveryZone() == null ||
                !deliveryAddress.equalsIgnoreCase(
                        restaurant.getDeliveryZone())) {

            throw new IllegalArgumentException(
                    "Restaurant does not deliver to this area"
            );
        }

        if (getTotal() < restaurant.getMinimumOrderValue()) {
            throw new IllegalArgumentException(
                    "Order total is below the minimum order value"
            );
        }

        status = "PENDING";
    }

    public void startPreparing() {

        if (!"PENDING".equals(status)) {
            throw new IllegalStateException(
                    "Only pending orders can start preparing"
            );
        }

        status = "PREPARING";
    }

    public void cancelOrder() {

        if (!"PENDING".equals(status)) {
            throw new IllegalStateException(
                    "Only pending orders can be cancelled"
            );
        }

        status = "CANCELLED";
    }
}