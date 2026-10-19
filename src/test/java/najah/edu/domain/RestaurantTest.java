package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class RestaurantTest {

    @Test
    void testRestaurantStartsWithEmptyMenu() {
        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        assertTrue(restaurant.getMenuItems().isEmpty());
    }

    @Test
    void testAddMenuItem() {
        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        MenuItem item =
                new MenuItem("Margherita", 12.5, true);

        restaurant.addMenuItem(item);

        List<MenuItem> menuItems = restaurant.getMenuItems();

        assertEquals(1, menuItems.size());
        assertSame(item, menuItems.get(0));
    }

    @Test
    void testAddingNullMenuItemIsRejected() {
        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        assertThrows(
                IllegalArgumentException.class,
                () -> restaurant.addMenuItem(null)
        );
    }

    @Test
    void testReturnedMenuCannotBeModified() {
        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        restaurant.addMenuItem(
                new MenuItem("Margherita", 12.5, true)
        );

        List<MenuItem> menuItems = restaurant.getMenuItems();

        assertThrows(
                UnsupportedOperationException.class,
                () -> menuItems.add(
                        new MenuItem("Pepperoni", 14.0, true)
                )
        );

        assertEquals(1, restaurant.getMenuItems().size());
    }
}