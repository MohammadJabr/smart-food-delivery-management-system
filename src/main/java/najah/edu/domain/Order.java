
package najah.edu.domain;

import java.util.ArrayList;

import java.util.List;

import najah.edu.notification.NotificationService;

import najah.edu.notification.OrderObserver;

import najah.edu.strategy.DeliveryCostStrategy;
import najah.edu.strategy.DistanceBasedDeliveryCostStrategy;
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

    /**
     * The list of observers registered to receive order updates.
     */
    private List<OrderObserver> observers = new ArrayList<>();

    /** The delivery distance in kilometers. */
    private double deliveryDistance;

    /** The calculated delivery fee. */
    private double deliveryFee;

    /** The final bill after applying a pricing strategy. */
    private Double pricedFinalBill;

    /** Strategy used to calculate this order's delivery fee. */
    private final DeliveryCostCalculator deliveryCostCalculator;

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
        this(restaurant, new DistanceBasedDeliveryCostStrategy());
    }

    /**
     * Creates an order using the supplied delivery-cost strategy.
     *
     * @param restaurant the restaurant for this order
     * @param deliveryCostStrategy the strategy used to calculate delivery fees
     * @throws IllegalArgumentException if the restaurant or strategy is invalid
     */
    public Order(
            Restaurant restaurant,
            DeliveryCostStrategy deliveryCostStrategy) {

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

        if (deliveryCostStrategy == null) {
            throw new IllegalArgumentException(
                    "Delivery cost strategy cannot be null"
            );
        }

        this.restaurant = restaurant;
        this.items = new ArrayList<>();
        this.status = "DRAFT";
        this.assignedDriver = null;
        this.deliveryCostCalculator =
                new DeliveryCostCalculator(deliveryCostStrategy);

        // The restaurant observes every update to its order.
        addObserver(restaurant);
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
        ensureNotDelivered();

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
        ensureNotDelivered();

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
     * Sets the notification service used alongside registered observers.
     *
     * Passing null disables this notification service,
     * but registered observers remain active.
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
        ensureNotDelivered();

        double calculatedFee =
                deliveryCostCalculator.calculateDeliveryCost(deliveryDistance);

        this.deliveryFee = calculatedFee;
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
 * Calculates the original bill before applying dynamic pricing.
 *
 * @return food total plus delivery fee
 */
public double getBaseFinalBill() {
    return getTotal() + deliveryFee;
}

/**
 * Returns the final bill after applying pricing.
 *
 * If no pricing strategy has been applied,
 * the original bill is returned.
 *
 * @return the final bill
 */
public double getFinalBill() {
    return pricedFinalBill != null
            ? pricedFinalBill.doubleValue()
            : getBaseFinalBill();
}

/**
 * Stores the final bill calculated by a pricing strategy.
 *
 * @param finalBill the calculated final amount
 * @throws IllegalArgumentException if the amount is invalid
 */
public void setPricedFinalBill(double finalBill) {
        ensureNotDelivered();

    if (!Double.isFinite(finalBill) || finalBill < 0) {
        throw new IllegalArgumentException(
                "Final bill must be non-negative and finite");
    }

    this.pricedFinalBill = finalBill;
}


    /**
     * Assigns an available driver to this order.
     *
     * The driver must be available, and no driver must have
     * already been assigned to this order.
     * The driver's status changes to BUSY after assignment.
     * Registered notification services and observers are notified when configured.
     *
     * @param driver the driver to assign to the order
     * @throws IllegalArgumentException if the driver is null or unavailable
     * @throws IllegalStateException if a driver is already assigned
     */
    public void assignDriver(Driver driver) {
        ensureNotDelivered();

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
        // The assigned driver observes subsequent order updates.
        addObserver(driver);

        	notifyObservers(
        		    "A driver has been assigned to your order: "
        		    + driver.getName()
        		);

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
        ensureNotDelivered();

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


        	notifyObservers("Order status changed to PENDING");

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
        notifyObservers("Order status changed to CONFIRMED");
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


        	notifyObservers("Order status changed to PREPARING");

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
        notifyObservers("Order status changed to CANCELLED");
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
        notifyObservers("Order status changed to READY");
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


        	notifyObservers("Order status changed to OUT_FOR_DELIVERY");

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


        	notifyObservers("Order status changed to DELIVERED");

    }

    /**
     * Registers an observer to receive notifications about this order.
     *
     * Duplicate observers are not added.
     *
     * @param observer the observer to register
     * @throws IllegalArgumentException if the observer is null
     */
    /**
     * Rejects changes to business data after an order is delivered.
     *
     * @throws IllegalStateException if this order is already delivered
     */
    protected final void ensureNotDelivered() {
        if ("DELIVERED".equals(status)) {
            throw new IllegalStateException(
                    "Delivered orders cannot be modified");
        }
    }

    public void addObserver(OrderObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer cannot be null");
        }

        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Removes an observer from this order.
     *
     * @param observer the observer to remove
     */
    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies the configured notification service and all registered observers.
     *
     * @param message the notification message
     */
    private void notifyObservers(String message) {

        if (notificationService != null) {
            notificationService.sendNotification(message);
        }

        for (OrderObserver observer : observers) {
            if (observer != notificationService) {
                observer.update(message);
            }
        }
    }
}
