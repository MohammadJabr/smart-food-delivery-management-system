package najah.edu.strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import najah.edu.domain.Driver;

public class FastestDriverStrategyTest {

    @Test
    public void testSelectsFastestAvailableDriver() {

        Driver rami = new Driver(
                "Rami", Driver.Status.AVAILABLE, 2.0, 15
        );

        Driver ahmad = new Driver(
                "Ahmad", Driver.Status.AVAILABLE, 5.0, 7
        );

        Driver sara = new Driver(
                "Sara", Driver.Status.BUSY, 1.0, 3
        );

        List<Driver> drivers = Arrays.asList(
                rami, ahmad, sara
        );

        FastestDriverStrategy strategy =
                new FastestDriverStrategy();

        Driver selected = strategy.selectDriver(drivers);

        assertSame(ahmad, selected);
    }

    @Test
    public void testThrowsWhenNoAvailableDriverExists() {

        Driver rami = new Driver(
                "Rami", Driver.Status.BUSY, 2.0, 10
        );

        Driver ahmad = new Driver(
                "Ahmad", Driver.Status.BUSY, 3.0, 5
        );

        List<Driver> drivers = Arrays.asList(
                rami, ahmad
        );

        FastestDriverStrategy strategy =
                new FastestDriverStrategy();

        assertThrows(
                IllegalStateException.class,
                () -> strategy.selectDriver(drivers)
        );
    }

    @Test
    public void testRejectsNullDriverList() {

        FastestDriverStrategy strategy =
                new FastestDriverStrategy();

        assertThrows(
                IllegalArgumentException.class,
                () -> strategy.selectDriver(null)
        );
    }
}