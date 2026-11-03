package najah.edu.strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import najah.edu.domain.Driver;

public class NearestDriverStrategyTest {

    @Test
    public void testSelectsNearestAvailableDriver() {

        Driver rami = new Driver(
                "Rami", Driver.Status.AVAILABLE, 5.0, 20
        );

        Driver ahmad = new Driver(
                "Ahmad", Driver.Status.BUSY, 1.0, 5
        );

        Driver sara = new Driver(
                "Sara", Driver.Status.AVAILABLE, 3.0, 15
        );

        List<Driver> drivers = Arrays.asList(
                rami, ahmad, sara
        );

        NearestDriverStrategy strategy =
                new NearestDriverStrategy();

        Driver selected = strategy.selectDriver(drivers);

        assertSame(sara, selected);
    }

    @Test
    public void testThrowsWhenNoAvailableDriverExists() {

        Driver rami = new Driver(
                "Rami", Driver.Status.BUSY, 2.0, 10
        );

        Driver ahmad = new Driver(
                "Ahmad", Driver.Status.BUSY, 3.0, 15
        );

        List<Driver> drivers = Arrays.asList(rami, ahmad);

        NearestDriverStrategy strategy =
                new NearestDriverStrategy();

        assertThrows(
                IllegalStateException.class,
                () -> strategy.selectDriver(drivers)
        );
    }

    @Test
    public void testRejectsNullDriverList() {

        NearestDriverStrategy strategy =
                new NearestDriverStrategy();

        assertThrows(
                IllegalArgumentException.class,
                () -> strategy.selectDriver(null)
        );
    }
}