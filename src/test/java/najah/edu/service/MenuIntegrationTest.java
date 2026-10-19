package najah.edu.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import najah.edu.domain.MenuItem;
import najah.edu.domain.Restaurant;
import najah.edu.repository.MenuRepository;
import najah.edu.repository.RestaurantRepository;

class MenuIntegrationTest {

    @Test
    void testLoadRestaurantMenuAndReturnAvailableItems()
            throws IOException {

        // Arrange: create temporary restaurant data
        File restaurantsFile = createTempFile(
                "restaurants_test",
                "Pizza House,true\n"
        );

        // Arrange: create temporary menu data
        File menuFile = createTempFile(
                "menu_test",
                "Pizza House,Margherita,12.50,true\n"
              + "Pizza House,Pepperoni,15.00,false\n"
        );

        RestaurantRepository restaurantRepository =
                new RestaurantRepository(
                        restaurantsFile.getAbsolutePath());

        List<Restaurant> restaurants =
                restaurantRepository.getRestaurants();

        MenuRepository menuRepository =
                new MenuRepository(menuFile.getAbsolutePath());

        // Act: load menu items into their restaurant
        menuRepository.loadMenuItems(restaurants);

        Restaurant restaurant = restaurants.get(0);

        MenuService menuService = new MenuService();

        List<MenuItem> availableItems =
                menuService.getAvailableMenuItems(restaurant);

        // Assert: only the available item is returned
        assertEquals(1, availableItems.size());

        assertEquals(
                "Margherita",
                availableItems.get(0).getName()
        );

        assertEquals(
                12.50,
                availableItems.get(0).getPrice(),
                0.0001
        );

        assertTrue(availableItems.get(0).isAvailable());
    }

    private File createTempFile(
            String prefix,
            String content) throws IOException {

        File file = File.createTempFile(prefix, ".txt");
        file.deleteOnExit();

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }

        return file;
    }
}