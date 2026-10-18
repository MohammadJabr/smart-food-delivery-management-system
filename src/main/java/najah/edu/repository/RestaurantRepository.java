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

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length == 2) {

                    String name = data[0].trim();
                    boolean open =
                            Boolean.parseBoolean(data[1].trim());

                    Restaurant restaurant =
                            new Restaurant(name, open);

                    restaurants.add(restaurant);
                }
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