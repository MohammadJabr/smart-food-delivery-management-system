
package najah.edu.service;

import java.time.LocalDateTime;
import java.util.List;

import najah.edu.domain.*;
import najah.edu.repository.OrderRepository;

public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {

        if (orderRepository == null) {
            throw new IllegalArgumentException(
                    "OrderRepository cannot be null");
        }

        this.orderRepository = orderRepository;
    }

    public Order createOrder(
            Restaurant restaurant,
            OrderType orderType) {

        if (orderType == null) {
            throw new IllegalArgumentException(
                    "Order type cannot be null");
        }

        switch (orderType) {

            case STANDARD:
                return new StandardOrder(restaurant);

            case EXPRESS:
                return new ExpressOrder(restaurant);

            case SCHEDULED:
                return new ScheduledOrder(restaurant);

            default:
                throw new IllegalArgumentException(
                        "Unsupported order type");
        }
    }

    public void addItem(
            Order order,
            MenuItem menuItem,
            int quantity) {

        validateOrder(order);

        order.addItem(menuItem, quantity);
    }

    public void setDeliveryDetails(
            Order order,
            String address,
            double distance) {

        validateOrder(order);

        if (!Double.isFinite(distance) || distance <= 0) {
            throw new IllegalArgumentException(
                    "Invalid delivery distance");
        }

        order.setDeliveryAddress(address);
        order.setDeliveryDistance(distance);
    }

    public void setScheduledDeliveryTime(
            Order order,
            LocalDateTime deliveryTime) {

        validateOrder(order);

        if (!(order instanceof ScheduledOrder)) {
            throw new IllegalArgumentException(
                    "This is not a scheduled order");
        }

        ScheduledOrder scheduledOrder =
                (ScheduledOrder) order;

        scheduledOrder.setScheduledDeliveryTime(deliveryTime);
    }

    public void placeOrder(Order order) {

        validateOrder(order);

        if (order instanceof ScheduledOrder) {

            ScheduledOrder scheduledOrder =
                    (ScheduledOrder) order;

            if (scheduledOrder.getScheduledDeliveryTime() == null) {
                throw new IllegalStateException(
                        "Scheduled delivery time is required");
            }
        }

        order.placeOrder();

        orderRepository.save(order);
    }

    public void cancelOrder(Order order) {

        validateOrder(order);

        order.cancelOrder();
    }

    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }

    public Order getOrderByNumber(int orderNumber) {

        return orderRepository.findByIndex(orderNumber);
    }
    

public void confirmOrder(Order order) {
    validateOrder(order);
    order.confirmOrder();
}

public void startPreparing(Order order) {
    validateOrder(order);
    order.startPreparing();
}

public void markReady(Order order) {
    validateOrder(order);
    order.markReady();
}

public void markOutForDelivery(Order order) {
    validateOrder(order);
    order.markOutForDelivery();
}

public void markDelivered(Order order) {
    validateOrder(order);
    order.markDelivered();
}


    private void validateOrder(Order order) {

        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null");
        }
    }
}
