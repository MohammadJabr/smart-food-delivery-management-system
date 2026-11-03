package najah.edu.strategy;

import java.util.List;
import najah.edu.domain.Driver;

public class NearestDriverStrategy implements DriverSelectionStrategy {

    @Override
    public Driver selectDriver(List<Driver> drivers) {

        if (drivers == null) {
            throw new IllegalArgumentException(
                    "Driver list cannot be null"
            );
        }

        Driver nearestDriver = null;

        for (Driver driver : drivers) {

            if (driver != null && driver.isAvailable()) {

                if (nearestDriver == null ||
                        driver.getDistanceToRestaurantKm()
                        < nearestDriver.getDistanceToRestaurantKm()) {

                    nearestDriver = driver;
                }
            }
        }

        if (nearestDriver == null) {
            throw new IllegalStateException(
                    "No available drivers"
            );
        }

        return nearestDriver;
    }
}