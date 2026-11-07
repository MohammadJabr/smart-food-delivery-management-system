package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BicycleDriverTest {

    @Test
    public void testBicycleDeliveryDistanceRules() {

        BicycleDriver driver =
                new BicycleDriver("Ahmad", Driver.Status.AVAILABLE);

        assertTrue(driver.canDeliverDistance(3.0));
        assertTrue(driver.canDeliverDistance(5.0));
        assertFalse(driver.canDeliverDistance(6.0));

        assertThrows(
                IllegalArgumentException.class,
                () -> driver.canDeliverDistance(0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> driver.canDeliverDistance(-2.0)
        );
    }
}