package najah.edu.domain;

import java.util.ArrayList;
import java.util.List;

import najah.edu.notification.NotificationService;

public class Order {

    private Restaurant restaurant;
    private List<OrderItem> items;
    private String status;
    private String deliveryAddress;
    private Driver assignedDriver;
    private NotificationService notificationService;

    // US4.2 - Delivery Cost
    private double deliveryDistance;
    private double deliveryFee;

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

    public void setNotificationService(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    // US4.2 - Set delivery distance and calculate delivery fee
    public void setDeliveryDistance(double deliveryDistance) {

        DeliveryCostCalculator calculator =
                new DeliveryCostCalculator();

        this.deliveryFee =
                calculator.calculateDeliveryCost(deliveryDistance);

        this.deliveryDistance = deliveryDistance;
    }

    public double getDeliveryDistance() {
        return deliveryDistance;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    // Food total + delivery fee
    public double getFinalBill() {
        return getTotal() + deliveryFee;
    }

    public void assignDriver(Driver driver) {

        if (driver == null) {
            throw new IllegalArgumentException(
                    "Driver cannot be null"
            );
        }

        if (assignedDriver != null) {
            throw new IllegalStateException(
                    "A driver is already assigned to this order"
            );
        }

        if (!driver.isAvailable()) {
            throw new IllegalArgumentException(
                    "Driver is not available"
            );
        }

        this.assignedDriver = driver;
        driver.markBusy();

        if (notificationService != null) {
            notificationService.sendNotification(
                    "A driver has been assigned to your order: "
                    + driver.getName()
            );
        }
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

    public void confirmOrder() {

        if (!"PENDING".equals(status)) {
            throw new IllegalStateException(
                    "Only pending orders can be confirmed"
            );
        }

        status = "CONFIRMED";
    }

    public void startPreparing() {

        if (!"CONFIRMED".equals(status)) {
            throw new IllegalStateException(
                    "Only confirmed orders can start preparing"
            );
        }

        status = "PREPARING";

        if (notificationService != null) {
            notificationService.sendNotification(
                    "Order status changed to PREPARING"
            );
        }
    }

    public void cancelOrder() {

        if (!"PENDING".equals(status)) {
            throw new IllegalStateException(
                    "Only pending orders can be cancelled"
            );
        }

        status = "CANCELLED";
    }

    public void markReady() {

        if (!"PREPARING".equals(status)) {
            throw new IllegalStateException(
                    "Only preparing orders can be marked as ready"
            );
        }

        status = "READY";
    }

    public void markOutForDelivery() {

        if (!"READY".equals(status)) {
            throw new IllegalStateException(
                    "Only ready orders can be marked as out for delivery"
            );
        }

        status = "OUT_FOR_DELIVERY";
    }

    public void markDelivered() {

        if (!"OUT_FOR_DELIVERY".equals(status)) {
            throw new IllegalStateException(
                    "Only orders out for delivery can be marked as delivered"
            );
        }

        status = "DELIVERED";
    }
}