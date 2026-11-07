package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExpressOrderTest {

    private ExpressOrder expressOrder;

    @BeforeEach
    public void setUp() {

        Restaurant restaurant =
                new Restaurant("Hulk Burger", true);

        MenuItem burger =
                new MenuItem("Burger", 20.0, true);

        restaurant.addMenuItem(burger);

        expressOrder = new ExpressOrder(restaurant);

        // 2 burgers = 40
        expressOrder.addItem(burger, 2);

        // 5 km × 2 = 10 delivery fee
        expressOrder.setDeliveryDistance(5.0);
    }

    @Test
    public void testExpressOrderType() {

        assertEquals(
                OrderType.EXPRESS,
                expressOrder.getOrderType()
        );
    }

    @Test
    public void testExpressOrderAddsAdditionalFee() {

        // Food = 40
        // Normal delivery = 10
        // Express fee = 10
        // Final bill = 60

        assertEquals(
                60.0,
                expressOrder.getFinalBill(),
                0.001
        );
    }
}