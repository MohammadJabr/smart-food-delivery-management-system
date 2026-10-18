package najah.edu.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import najah.edu.domain.Restaurant;

class RestaurantRepositoryTest {

    @Test
    void testLoadRestaurants() throws IOException {

        File file = File.createTempFile("restaurants_test", ".txt");
        file.deleteOnExit();

        FileWriter writer = new FileWriter(file);

        writer.write("Pizza House,true\n");
        writer.write("Burger House,false\n");
        writer.write("Palestine Kitchen,true\n");

        writer.close();

        RestaurantRepository repository =
                new RestaurantRepository(file.getAbsolutePath());

        ArrayList<Restaurant> restaurants =
                repository.getRestaurants();

        assertEquals(3, restaurants.size());

        assertEquals("Pizza House",
                restaurants.get(0).getName());

        assertTrue(restaurants.get(0).isOpen());

        assertEquals("Burger House",
                restaurants.get(1).getName());

        assertFalse(restaurants.get(1).isOpen());

        assertEquals("Palestine Kitchen",
                restaurants.get(2).getName());

        assertTrue(restaurants.get(2).isOpen());
    }
}