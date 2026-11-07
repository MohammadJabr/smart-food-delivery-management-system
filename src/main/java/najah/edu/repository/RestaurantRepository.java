
package najah.edu.repository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import najah.edu.domain.Restaurant;

public class RestaurantRepository {

    private ArrayList<Restaurant> restaurants =
            new ArrayList<Restaurant>();

    private String fileName;

    public RestaurantRepository(String fileName) {
        this.fileName = fileName;
        loadRestaurants();
    }

    public void loadRestaurants() {

        restaurants.clear();

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

                if (data.length != 2 && data.length != 4) {
                    throw new IllegalStateException(
                            "Invalid restaurant data at line "
                            + lineNumber);
                }

                String name = data[0].trim();
                String openText = data[1].trim();

                if (name.isEmpty()
                        || !(openText.equalsIgnoreCase("true")
                        || openText.equalsIgnoreCase("false"))) {

                    throw new IllegalStateException(
                            "Invalid restaurant information at line "
                            + lineNumber);
                }

                boolean open = Boolean.parseBoolean(openText);

                Restaurant restaurant =
                        new Restaurant(name, open);

                if (data.length == 4) {

                    String deliveryZone = data[2].trim();

                    if (deliveryZone.isEmpty()) {
                        throw new IllegalStateException(
                                "Delivery zone cannot be empty at line "
                                + lineNumber);
                    }

                    double minimumOrderValue;

                    try {
                        minimumOrderValue =
                                Double.parseDouble(data[3].trim());

                    } catch (NumberFormatException e) {
                        throw new IllegalStateException(
                                "Invalid minimum order value at line "
                                + lineNumber, e);
                    }

                    restaurant.setDeliveryZone(deliveryZone);

                    restaurant.setMinimumOrderValue(
                            minimumOrderValue);
                }

                restaurants.add(restaurant);
            }

        } catch (FileNotFoundException e) {

            throw new IllegalStateException(
                    "Restaurants file not found", e);

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Error reading restaurants file", e);
        }
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }
}
