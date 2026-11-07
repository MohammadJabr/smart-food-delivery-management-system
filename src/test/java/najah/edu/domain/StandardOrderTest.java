package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StandardOrderTest {

    private Restaurant restaurant;
    private StandardOrder standardOrder;
    private MenuItem burger;

    @BeforeEach
    public void setUp() {

        restaurant = new Restaurant("Hulk Burger", true);

        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(10.0);

        burger = new MenuItem("Burger", 20.0, true);
        restaurant.addMenuItem(burger);

        standardOrder = new StandardOrder(restaurant);
    }

    @Test
    public void testStandardOrderType() {

        assertEquals(
                OrderType.STANDARD,
                standardOrder.getOrderType()
        );
    }

    @Test
    public void testStandardOrderFollowsNormalDeliveryPolicies() {

        standardOrder.addItem(burger, 1);
        standardOrder.setDeliveryAddress("Nablus");

        standardOrder.placeOrder();

        assertEquals(
                "PENDING",
                standardOrder.getStatus()
        );
    }

    @Test
    public void testStandardOrderRejectsOutsideDeliveryZone() {

        standardOrder.addItem(burger, 1);
        standardOrder.setDeliveryAddress("Ramallah");

        assertThrows(
                IllegalArgumentException.class,
                () -> standardOrder.placeOrder()
        );
    }
}