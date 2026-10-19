package najah.edu.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import najah.edu.domain.MenuItem;
import najah.edu.domain.Restaurant;

class MenuServiceTest {

    @Test
    void testOnlyAvailableMenuItemsAreReturned() {
        // Arrange
        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        restaurant.addMenuItem(
                new MenuItem("Margherita", 12.5, true));

        restaurant.addMenuItem(
                new MenuItem("Pepperoni", 15.0, false));

        restaurant.addMenuItem(
                new MenuItem("Vegetarian", 13.0, true));

        MenuService service = new MenuService();

        // Act
        List<MenuItem> availableItems =
                service.getAvailableMenuItems(restaurant);

        // Assert
        assertEquals(2, availableItems.size());

        assertEquals("Margherita",
                availableItems.get(0).getName());

        assertEquals(12.5,
                availableItems.get(0).getPrice(), 0.0001);

        assertEquals("Vegetarian",
                availableItems.get(1).getName());

        assertEquals(13.0,
                availableItems.get(1).getPrice(), 0.0001);

        assertTrue(availableItems.get(0).isAvailable());
        assertTrue(availableItems.get(1).isAvailable());
    }

    @Test
    void testNoAvailableItemsReturnsEmptyList() {
        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        restaurant.addMenuItem(
                new MenuItem("Margherita", 12.5, false));

        restaurant.addMenuItem(
                new MenuItem("Pepperoni", 15.0, false));

        MenuService service = new MenuService();

        List<MenuItem> availableItems =
                service.getAvailableMenuItems(restaurant);

        assertTrue(availableItems.isEmpty());
    }

    @Test
    void testEmptyMenuReturnsEmptyList() {
        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        MenuService service = new MenuService();

        List<MenuItem> availableItems =
                service.getAvailableMenuItems(restaurant);

        assertTrue(availableItems.isEmpty());
    }

    @Test
    void testNullRestaurantIsRejected() {
        MenuService service = new MenuService();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.getAvailableMenuItems(null)
        );
    }
}