package najah.edu.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import najah.edu.domain.MenuItem;
import najah.edu.domain.Restaurant;

/**
 * Loads restaurant menu items from a text file.
 */
public class MenuRepository {

    private final String fileName;

    public MenuRepository(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "File name cannot be empty");
        }

        this.fileName = fileName;
    }

    /**
     * Loads menu items and attaches them to their restaurants.
     *
     * Each file line must contain:
     * restaurantName,itemName,price,available
     *
     * @param restaurants restaurants that will receive the menu items
     * @throws IllegalArgumentException if the restaurant list is null
     * @throws IllegalStateException if the file or its data is invalid
     */
    public void loadMenuItems(List<Restaurant> restaurants) {

        if (restaurants == null) {
            throw new IllegalArgumentException(
                    "Restaurants list cannot be null");
        }

        Map<String, Restaurant> restaurantsByName = new HashMap<>();

        for (Restaurant restaurant : restaurants) {
            if (restaurant == null) {
                throw new IllegalArgumentException(
                        "Restaurant list cannot contain null");
            }

            restaurantsByName.put(
                    restaurant.getName(), restaurant);
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(fileName))) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split(",", -1);

                if (data.length != 4) {
                    throw new IllegalStateException(
                            "Invalid menu data at line " + lineNumber);
                }

                String restaurantName = data[0].trim();
                String itemName = data[1].trim();
                String priceText = data[2].trim();
                String availableText =
                        data[3].trim().toLowerCase(Locale.ROOT);

                if (restaurantName.isEmpty() || itemName.isEmpty()) {
                    throw new IllegalStateException(
                            "Restaurant and item names cannot be empty"
                                    + " at line " + lineNumber);
                }

                if (!availableText.equals("true")
                        && !availableText.equals("false")) {
                    throw new IllegalStateException(
                            "Invalid availability at line " + lineNumber);
                }

                Restaurant restaurant =
                        restaurantsByName.get(restaurantName);

                if (restaurant == null) {
                    throw new IllegalStateException(
                            "Unknown restaurant '" + restaurantName
                                    + "' at line " + lineNumber);
                }

                double price;

                try {
                    price = Double.parseDouble(priceText);
                } catch (NumberFormatException e) {
                    throw new IllegalStateException(
                            "Invalid price at line " + lineNumber, e);
                }

                try {
                    MenuItem item = new MenuItem(
                            itemName,
                            price,
                            Boolean.parseBoolean(availableText));

                    restaurant.addMenuItem(item);

                } catch (IllegalArgumentException e) {
                    throw new IllegalStateException(
                            "Invalid menu item at line " + lineNumber, e);
                }
            }

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not read menu file: " + fileName, e);
        }
    }
}