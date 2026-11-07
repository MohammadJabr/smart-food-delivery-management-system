
package najah.edu.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import najah.edu.domain.Order;

public class OrderRepository {

    private final List<Order> orders = new ArrayList<>();

    public void save(Order order) {

        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null");
        }

        if (orders.contains(order)) {
            throw new IllegalArgumentException(
                    "Order already exists");
        }

        orders.add(order);
    }

    public List<Order> findAll() {

        return Collections.unmodifiableList(
                new ArrayList<>(orders));
    }

    public Order findByIndex(int index) {

        if (index < 1 || index > orders.size()) {
            throw new IllegalArgumentException(
                    "Invalid order number");
        }

        return orders.get(index - 1);
    }

    public int count() {
        return orders.size();
    }
}
