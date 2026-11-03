package najah.edu.service;

import java.util.List;

import najah.edu.domain.Driver;
import najah.edu.domain.Order;
import najah.edu.strategy.DriverSelectionStrategy;

public class DriverAssignmentService {

    private final DriverSelectionStrategy strategy;

    public DriverAssignmentService(DriverSelectionStrategy strategy) {

        if (strategy == null) {
            throw new IllegalArgumentException(
                    "Driver selection strategy cannot be null"
            );
        }

        this.strategy = strategy;
    }

    public Driver assignDriver(Order order, List<Driver> drivers) {

        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null"
            );
        }

        Driver selectedDriver = strategy.selectDriver(drivers);

        order.assignDriver(selectedDriver);

        return selectedDriver;
    }
}