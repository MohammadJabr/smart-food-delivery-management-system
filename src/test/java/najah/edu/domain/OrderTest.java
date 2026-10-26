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

        // Delivery policy
        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(10.0);
        order.setDeliveryAddress("Nablus");

        order.placeOrder();

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

    @Test
    public void testCannotAddUnavailableMenuItem() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        MenuItem pizza =
                new MenuItem("Pepperoni Pizza", 15.0, false);

        restaurant.addMenuItem(pizza);

        Order order = new Order(restaurant);

        assertThrows(
                IllegalArgumentException.class,
                () -> order.addItem(pizza, 1)
        );
    }

    @Test
    public void testCannotAddItemWithNegativeQuantity() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        MenuItem pizza =
                new MenuItem("Pizza", 8.0, true);

        restaurant.addMenuItem(pizza);

        Order order = new Order(restaurant);

        assertThrows(
                IllegalArgumentException.class,
                () -> order.addItem(pizza, -1)
        );
    }

    @Test
    public void testCannotPlaceEmptyOrder() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        Order order = new Order(restaurant);

        assertThrows(
                IllegalArgumentException.class,
                () -> order.placeOrder()
        );

        assertEquals("DRAFT", order.getStatus());
    }

    // US2.3 - Restaurant outside delivery zone
    @Test
    public void testCannotOrderOutsideDeliveryZone() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        restaurant.setDeliveryZone("Nablus");

        MenuItem pizza =
                new MenuItem("Pizza", 8.0, true);

        restaurant.addMenuItem(pizza);

        Order order = new Order(restaurant);

        order.addItem(pizza, 2);
        order.setDeliveryAddress("Ramallah");

        assertThrows(
                IllegalArgumentException.class,
                () -> order.placeOrder()
        );

        assertEquals("DRAFT", order.getStatus());
    }

    // US2.3 - Order below minimum value
    @Test
    public void testCannotOrderBelowMinimumValue() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(20.0);

        MenuItem pizza =
                new MenuItem("Pizza", 8.0, true);

        restaurant.addMenuItem(pizza);

        Order order = new Order(restaurant);

        order.addItem(pizza, 2);
        order.setDeliveryAddress("Nablus");

        assertThrows(
                IllegalArgumentException.class,
                () -> order.placeOrder()
        );

        assertEquals("DRAFT", order.getStatus());
    }

    // US2.3 - Invalid delivery address
    @Test
    public void testCannotUseInvalidDeliveryAddress() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        Order order = new Order(restaurant);

        assertThrows(
                IllegalArgumentException.class,
                () -> order.setDeliveryAddress("")
        );
    }
}