package najah.edu.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import najah.edu.domain.Restaurant;

class RestaurantServiceTest {

    @Test
    void testOnlyOpenRestaurantsAreDisplayed() {

        ArrayList<Restaurant> restaurants =
                new ArrayList<Restaurant>();

        restaurants.add(
                new Restaurant("Pizza House", true));

        restaurants.add(
                new Restaurant("Burger House", false));

        restaurants.add(
                new Restaurant("Palestine Kitchen", true));

        RestaurantService service =
                new RestaurantService(restaurants);

        ArrayList<Restaurant> availableRestaurants =
                service.getAvailableRestaurants();

        // Two restaurants are open
        assertEquals(2, availableRestaurants.size());

        // Open restaurants are displayed
        assertEquals(
                "Pizza House",
                availableRestaurants.get(0).getName()
        );

        assertEquals(
                "Palestine Kitchen",
                availableRestaurants.get(1).getName()
        );

        // Closed restaurant is hidden
        for (Restaurant restaurant : availableRestaurants) {

            assertNotEquals(
                    "Burger House",
                    restaurant.getName()
            );

            assertTrue(restaurant.isOpen());
        }
    }
}