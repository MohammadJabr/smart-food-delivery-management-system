package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class OrderTest {

    @Test
    public void testCreateOrderAndCalculateTotal() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        MenuItem pizza =
                new MenuItem("Pizza", 8.0, true);

        restaurant.addMenuItem(pizza);

        Order order = new Order(restaurant);

        order.addItem(pizza, 2);

        assertEquals(16.0, order.getTotal());

        assertEquals("PENDING", order.getStatus());
    }
    @Test
    public void testCannotAddItemNotInRestaurantMenu() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        MenuItem burger =
                new MenuItem("Burger", 5.0, true);

        Order order = new Order(restaurant);

        assertThrows(
                IllegalArgumentException.class,
                () -> order.addItem(burger, 1)
        );
    }
    @Test
    public void testCannotAddItemWithInvalidQuantity() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        MenuItem pizza =
                new MenuItem("Pizza", 8.0, true);

        restaurant.addMenuItem(pizza);

        Order order = new Order(restaurant);

        assertThrows(
                IllegalArgumentException.class,
                () -> order.addItem(pizza, 0)
        );
    }
}