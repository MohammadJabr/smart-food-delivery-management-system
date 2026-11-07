package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MotorcycleDriverTest {

    @Test
    public void testMotorcycleDeliveryDistanceRules() {

        MotorcycleDriver driver =
                new MotorcycleDriver("Ahmad", Driver.Status.AVAILABLE);


        assertTrue(driver.canDeliverDistance(10.0));
        assertTrue(driver.canDeliverDistance(15.0));

     
        assertFalse(driver.canDeliverDistance(16.0));

        
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