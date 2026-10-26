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

        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(16.0);
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
    
    @Test
    public void testCancelPendingOrder() {
        Restaurant restaurant = new Restaurant("Pizza House", true);
        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(10.0);

        MenuItem pizza = new MenuItem("Pizza", 8.0, true);
        restaurant.addMenuItem(pizza);

        Order order = new Order(restaurant);
        order.addItem(pizza, 2);
        order.setDeliveryAddress("Nablus");

        order.placeOrder();
        assertEquals("PENDING", order.getStatus());

        order.cancelOrder();

        assertEquals("CANCELLED", order.getStatus());
    }
    
    @Test
    public void testCannotCancelDraftOrder() {
        Restaurant restaurant = new Restaurant("Pizza House", true);
        Order order = new Order(restaurant);

        assertThrows(
            IllegalStateException.class,
            () -> order.cancelOrder()
        );

        assertEquals("DRAFT", order.getStatus());
    }
    
    @Test
    public void testCannotCancelOrderOncePreparing() {
        Restaurant restaurant = new Restaurant("Pizza House", true);
        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(10.0);

        MenuItem pizza = new MenuItem("Pizza", 8.0, true);
        restaurant.addMenuItem(pizza);

        Order order = new Order(restaurant);
        order.addItem(pizza, 2);
        order.setDeliveryAddress("Nablus");

        order.placeOrder();
        order.startPreparing();

        assertEquals("PREPARING", order.getStatus());

        assertThrows(
            IllegalStateException.class,
            () -> order.cancelOrder()
        );

        assertEquals("PREPARING", order.getStatus());
    }
}