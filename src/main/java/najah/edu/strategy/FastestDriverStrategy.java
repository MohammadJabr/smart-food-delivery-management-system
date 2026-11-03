package najah.edu.strategy;

import java.util.List;
import najah.edu.domain.Driver;

public class FastestDriverStrategy implements DriverSelectionStrategy {

    @Override
    public Driver selectDriver(List<Driver> drivers) {

        if (drivers == null) {
            throw new IllegalArgumentException(
                    "Driver list cannot be null"
            );
        }

        Driver fastestDriver = null;

        for (Driver driver : drivers) {

            if (driver != null && driver.isAvailable()) {

                if (fastestDriver == null ||
                        driver.getEstimatedArrivalMinutes()
                        < fastestDriver.getEstimatedArrivalMinutes()) {

                    fastestDriver = driver;
                }
            }
        }

        if (fastestDriver == null) {
            throw new IllegalStateException(
                    "No available drivers"
            );
        }

        return fastestDriver;
    }
}