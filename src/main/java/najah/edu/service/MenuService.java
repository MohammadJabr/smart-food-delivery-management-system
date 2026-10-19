package najah.edu.service;

import java.util.ArrayList;
import java.util.List;

import najah.edu.domain.MenuItem;
import najah.edu.domain.Restaurant;

public class MenuService {

    public List<MenuItem> getAvailableMenuItems(Restaurant restaurant) {

        if (restaurant == null) {
            throw new IllegalArgumentException(
                    "Restaurant cannot be null"
            );
        }

        List<MenuItem> availableItems = new ArrayList<>();

        for (MenuItem item : restaurant.getMenuItems()) {
            if (item.isAvailable()) {
                availableItems.add(item);
            }
        }

        return availableItems;
    }
}