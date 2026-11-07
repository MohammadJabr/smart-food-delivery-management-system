
package najah.edu.service;

import java.util.ArrayList;
import java.util.List;

import najah.edu.domain.CarDriver;
import najah.edu.domain.Driver;
import najah.edu.domain.Order;
import najah.edu.domain.OrderItem;
import najah.edu.strategy.DriverSelectionStrategy;

public class DriverAssignmentService {

    private final DriverSelectionStrategy strategy;

    public DriverAssignmentService(DriverSelectionStrategy strategy) {

        if (strategy == null) {
            throw new IllegalArgumentException(
                    "Driver selection strategy cannot be null");
        }

        this.strategy = strategy;
    }

    public Driver assignDriver(Order order, List<Driver> drivers) {

        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null");
        }

        if (drivers == null) {
            throw new IllegalArgumentException(
                    "Driver list cannot be null");
        }

        List<Driver> eligibleDrivers = new ArrayList<>();

        int totalQuantity = 0;

        for (OrderItem item : order.getItems()) {
            totalQuantity += item.getQuantity();
        }

        for (Driver driver : drivers) {

            if (driver == null || !driver.isAvailable()) {
                continue;
            }

            boolean eligible = true;

            double distance = order.getDeliveryDistance();

            if (distance > 0
                    && !driver.canDeliverDistance(distance)) {
                eligible = false;
            }

            if (driver instanceof CarDriver && totalQuantity > 0) {

                CarDriver car = (CarDriver) driver;

                if (!car.canHandleOrderQuantity(totalQuantity)) {
                    eligible = false;
                }
            }

            if (eligible) {
                eligibleDrivers.add(driver);
            }
        }

        if (eligibleDrivers.isEmpty()) {
            throw new IllegalStateException(
                    "No available drivers can handle this order");
        }

        Driver selectedDriver =
                strategy.selectDriver(eligibleDrivers);

        order.assignDriver(selectedDriver);

        return selectedDriver;
    }
}
