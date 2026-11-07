
package najah.edu.domain;

import java.util.ArrayList;
import java.util.List;

import najah.edu.notification.NotificationService;

/**
 * Represents a customer food order in the Smart Food Delivery System.
 *
 * An order contains a restaurant, a list of ordered items,
 * delivery information, its current status, and an assigned driver.
 *
 * The class manages the order lifecycle, calculates the order total
 * and delivery cost, and sends notifications when important
 * order events occur.
 *
 * @author Smart Food Delivery Team
 * @version 1.0
 */
public class Order {

    /** The restaurant from which the customer places the order. */
    private Restaurant restaurant;

    /** The list of items included in this order. */
    private List<OrderItem> items;

    /** The current status of the order. */
    private String status;

    /** The delivery address provided by the customer. */
    private String deliveryAddress;

    /** The driver assigned to deliver this order. */
    private Driver assignedDriver;

    /** The service responsible for sending order notifications. */
    private NotificationService notificationService;

    /** The delivery distance in kilometers. */
    private double deliveryDistance;

    /** The calculated delivery fee. */
    private double deliveryFee;

    /**
     * Creates a new order for the specified restaurant.
     *
     * The restaurant must not be null and must be open.
     * A new order starts with DRAFT status and an empty item list.
     *
     * @param restaurant the restaurant from which the order is placed
     * @throws IllegalArgumentException if the restaurant is null or closed
     */
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

    /**
     * Adds a menu item to this order with the specified quantity.
     *
     * The menu item must belong to the selected restaurant
     * and must be available.
     *
     * @param menuItem the menu item to add
     * @param quantity the quantity of the menu item
     * @throws IllegalArgumentException if the item is null, unavailable,
     *         does not belong to the restaurant, or has an invalid quantity
     */
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

    /**
     * Calculates the total price of all items in this order.
     *
     * @return the total price of the ordered items
     */
    public double getTotal() {

        double total = 0;

        for (OrderItem item : items) {
            total += item.getSubtotal();
        }

        return total;
    }

    /**
     * Retrieves the restaurant associated with this order.
     *
     * @return the restaurant from which the order was placed
     */
    public Restaurant getRestaurant() {
        return restaurant;
    }

    /**
     * Retrieves the items included in this order.
     *
     * A new list is returned to prevent direct modification
     * of the order's internal item list.
     *
     * @return a copy of the order items
     */
    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * Retrieves the current status of this order.
     *
     * @return the current order status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Retrieves the type of this order.
     *
     * The default order type is STANDARD.
     * Subclasses may override this method to provide
     * their specific order types.
     *
     * @return the type of the order
     */
    public OrderType getOrderType() {
        return OrderType.STANDARD;
    }

    /**
     * Retrieves the delivery address of this order.
     *
     * @return the delivery address, or null if not yet assigned
     */
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * Sets the delivery address for this order.
     *
     * The address cannot be null or empty.
     *
     * @param deliveryAddress the customer's delivery address
     * @throws IllegalArgumentException if the address is null or empty
     */
    public void setDeliveryAddress(String deliveryAddress) {

        if (deliveryAddress == null ||
                deliveryAddress.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Delivery address cannot be empty"
            );
        }

        this.deliveryAddress = deliveryAddress.trim();
    }

    /**
     * Retrieves the driver assigned to this order.
     *
     * @return the assigned driver, or null if no driver is assigned
     */
    public Driver getAssignedDriver() {
        return assignedDriver;
    }

    /**
     * Sets the notification service used to send order updates.
     *
     * Passing null disables notifications for this order.
     *
     * @param notificationService the notification service to use
     */
    public void setNotificationService(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    /**
     * Sets the delivery distance and calculates the delivery fee.
     *
     * The delivery fee is calculated using DeliveryCostCalculator.
     *
     * @param deliveryDistance the delivery distance in kilometers
     * @throws IllegalArgumentException if the delivery distance is invalid
     */
    public void setDeliveryDistance(double deliveryDistance) {

        DeliveryCostCalculator calculator =
                new DeliveryCostCalculator();

        this.deliveryFee =
                calculator.calculateDeliveryCost(deliveryDistance);

        this.deliveryDistance = deliveryDistance;
    }

    /**
     * Retrieves the delivery distance of this order.
     *
     * @return the delivery distance in kilometers
     */
    public double getDeliveryDistance() {
        return deliveryDistance;
    }

    /**
     * Retrieves the calculated delivery fee.
     *
     * @return the delivery fee
     */
    public double getDeliveryFee() {
        return deliveryFee;
    }

    /**
     * Calculates the final bill by adding the food total
     * and the delivery fee.
     *
     * @return the final order bill
     */
    public double getFinalBill() {
        return getTotal() + deliveryFee;
    }

    /**
     * Assigns an available driver to this order.
     *
     * The driver must be available, and no driver must have
     * already been assigned to this order.
     * The driver's status changes to BUSY after assignment.
     * A notification is sent if a notification service is configured.
     *
     * @param driver the driver to assign to the order
     * @throws IllegalArgumentException if the driver is null or unavailable
     * @throws IllegalStateException if a driver is already assigned
     */
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

    /**
     * Places the order after validating its contents and delivery details.
     *
     * The order must contain at least one item, have a valid delivery
     * address, belong to the restaurant's delivery zone, and meet
     * the restaurant's minimum order value.
     *
     * The order status changes from DRAFT to PENDING when successful.
     *
     * @throws IllegalArgumentException if the order is empty, the delivery
     *         address is missing or invalid, or the minimum order value
     *         is not satisfied
     */
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

    /**
     * Confirms a pending order.
     *
     * The order status changes from PENDING to CONFIRMED.
     *
     * @throws IllegalStateException if the order is not in PENDING status
     */
    public void confirmOrder() {

        if (!"PENDING".equals(status)) {
            throw new IllegalStateException(
                    "Only pending orders can be confirmed"
            );
        }

        status = "CONFIRMED";
    }

    /**
     * Starts preparing a confirmed order.
     *
     * The order status changes from CONFIRMED to PREPARING.
     * A notification is sent if a notification service is configured.
     *
     * @throws IllegalStateException if the order is not in CONFIRMED status
     */
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

    /**
     * Cancels a pending order.
     *
     * Only orders in PENDING status can be cancelled.
     * The order status changes to CANCELLED.
     *
     * @throws IllegalStateException if the order is not in PENDING status
     */
    public void cancelOrder() {

        if (!"PENDING".equals(status)) {
            throw new IllegalStateException(
                    "Only pending orders can be cancelled"
            );
        }

        status = "CANCELLED";
    }

    /**
     * Marks an order as ready after preparation.
     *
     * The order status changes from PREPARING to READY.
     *
     * @throws IllegalStateException if the order is not in PREPARING status
     */
    public void markReady() {

        if (!"PREPARING".equals(status)) {
            throw new IllegalStateException(
                    "Only preparing orders can be marked as ready"
            );
        }

        status = "READY";
    }

    /**
     * Marks a ready order as out for delivery.
     *
     * The order status changes from READY to OUT_FOR_DELIVERY.
     * A notification is sent if a notification service is configured.
     *
     * @throws IllegalStateException if the order is not in READY status
     */
    public void markOutForDelivery() {

        if (!"READY".equals(status)) {
            throw new IllegalStateException(
                    "Only ready orders can be marked as out for delivery"
            );
        }

        status = "OUT_FOR_DELIVERY";

        if (notificationService != null) {
            notificationService.sendNotification(
                    "Order status changed to OUT_FOR_DELIVERY"
            );
        }
    }

    /**
     * Marks an order as delivered.
     *
     * The order status changes from OUT_FOR_DELIVERY to DELIVERED.
     * A notification is sent if a notification service is configured.
     *
     * @throws IllegalStateException if the order is not
     *         in OUT_FOR_DELIVERY status
     */
    public void markDelivered() {

        if (!"OUT_FOR_DELIVERY".equals(status)) {
            throw new IllegalStateException(
                    "Only orders out for delivery can be marked as delivered"
            );
        }

        status = "DELIVERED";

        if (notificationService != null) {
            notificationService.sendNotification(
                    "Order status changed to DELIVERED"
            );
        }
    }
}
