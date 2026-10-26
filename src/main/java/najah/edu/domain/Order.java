package najah.edu.domain;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private Restaurant restaurant;
    private List<OrderItem> items;
    private String status;

    public Order(Restaurant restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null");
        }

        if (!restaurant.isOpen()) {
            throw new IllegalArgumentException("Cannot place an order from a closed restaurant");
        }

        this.restaurant = restaurant;
        this.items = new ArrayList<>();
        this.status = "DRAFT";
    }

    public void addItem(MenuItem menuItem, int quantity) {
        if (menuItem == null) {
            throw new IllegalArgumentException("Menu item cannot be null");
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

        OrderItem orderItem = new OrderItem(menuItem, quantity);
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
    
    public void placeOrder() {
        if (items.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot place an empty order"
            );
        }

        status = "PENDING";
    }
}