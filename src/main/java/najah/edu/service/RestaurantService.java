package najah.edu.service;

import java.util.ArrayList;

import najah.edu.domain.Restaurant;

public class RestaurantService {

    private ArrayList<Restaurant> restaurants;

    public RestaurantService(ArrayList<Restaurant> restaurants) {

        if (restaurants == null) {
            throw new IllegalArgumentException(
                    "Restaurants list cannot be null"
            );
        }

        this.restaurants = restaurants;
    }

    public ArrayList<Restaurant> getAvailableRestaurants() {

        ArrayList<Restaurant> availableRestaurants =
                new ArrayList<Restaurant>();

        for (Restaurant restaurant : restaurants) {

            if (restaurant.isOpen()) {
                availableRestaurants.add(restaurant);
            }
        }

        return availableRestaurants;
    }
}