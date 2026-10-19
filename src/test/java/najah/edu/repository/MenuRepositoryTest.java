package najah.edu.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import najah.edu.domain.MenuItem;
import najah.edu.domain.Restaurant;

class MenuRepositoryTest {

    private File createMenuFile(String content) throws IOException {
        File file = File.createTempFile("menu_test", ".txt");
        file.deleteOnExit();

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }

        return file;
    }

    @Test
    void testLoadMenuItems() throws IOException {
        File file = createMenuFile(
                "Pizza House,Margherita,12.50,true\n"
              + "Pizza House,Pepperoni,15.00,false\n"
              + "Burger House,Classic Burger,10.00,true\n"
        );

        Restaurant pizzaHouse =
                new Restaurant("Pizza House", true);

        Restaurant burgerHouse =
                new Restaurant("Burger House", false);

        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(pizzaHouse);
        restaurants.add(burgerHouse);

        MenuRepository repository =
                new MenuRepository(file.getAbsolutePath());

        repository.loadMenuItems(restaurants);

        // Verify the menu items belong to the correct restaurant
        assertEquals(2, pizzaHouse.getMenuItems().size());
        assertEquals(1, burgerHouse.getMenuItems().size());

        // Verify the first pizza
        MenuItem margherita = pizzaHouse.getMenuItems().get(0);

        assertEquals("Margherita", margherita.getName());
        assertEquals(12.50, margherita.getPrice(), 0.0001);
        assertTrue(margherita.isAvailable());

        // Verify the second pizza is unavailable
        MenuItem pepperoni = pizzaHouse.getMenuItems().get(1);

        assertEquals("Pepperoni", pepperoni.getName());
        assertEquals(15.00, pepperoni.getPrice(), 0.0001);
        assertFalse(pepperoni.isAvailable());

        // Verify the burger belongs to Burger House
        MenuItem burger = burgerHouse.getMenuItems().get(0);

        assertEquals("Classic Burger", burger.getName());
        assertEquals(10.00, burger.getPrice(), 0.0001);
        assertTrue(burger.isAvailable());
    }

    @Test
    void testInvalidAvailabilityIsRejected() throws IOException {
        File file = createMenuFile(
                "Pizza House,Margherita,12.50,maybe\n"
        );

        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(new Restaurant("Pizza House", true));

        MenuRepository repository =
                new MenuRepository(file.getAbsolutePath());

        assertThrows(
                IllegalStateException.class,
                () -> repository.loadMenuItems(restaurants)
        );
    }

    @Test
    void testUnknownRestaurantIsRejected() throws IOException {
        File file = createMenuFile(
                "Unknown Restaurant,Margherita,12.50,true\n"
        );

        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(new Restaurant("Pizza House", true));

        MenuRepository repository =
                new MenuRepository(file.getAbsolutePath());

        assertThrows(
                IllegalStateException.class,
                () -> repository.loadMenuItems(restaurants)
        );
    }
}