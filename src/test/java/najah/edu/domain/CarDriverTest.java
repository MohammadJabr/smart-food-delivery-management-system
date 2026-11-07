package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CarDriverTest {

    @Test
    public void testCarOrderQuantityRules() {

        CarDriver driver =
                new CarDriver("Ahmad", Driver.Status.AVAILABLE);

        assertTrue(driver.canHandleOrderQuantity(10));
        assertTrue(driver.canHandleOrderQuantity(20));

      
        assertFalse(driver.canHandleOrderQuantity(21));

      
        assertThrows(
                IllegalArgumentException.class,
                () -> driver.canHandleOrderQuantity(0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> driver.canHandleOrderQuantity(-2)
        );
    }
}