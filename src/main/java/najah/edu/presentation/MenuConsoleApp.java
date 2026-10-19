package najah.edu.presentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import najah.edu.domain.MenuItem;
import najah.edu.domain.Restaurant;
import najah.edu.repository.MenuRepository;
import najah.edu.repository.RestaurantRepository;
import najah.edu.service.MenuService;
import najah.edu.service.RestaurantService;

public class MenuConsoleApp {

    public static void main(String[] args) {

        ArrayList<Restaurant> restaurants;

        try {
            RestaurantRepository restaurantRepository =
                    new RestaurantRepository("restaurants.txt");

            restaurants = restaurantRepository.getRestaurants();

            MenuRepository menuRepository =
                    new MenuRepository("menu.txt");

            menuRepository.loadMenuItems(restaurants);

        } catch (IllegalStateException | IllegalArgumentException e) {
            System.err.println(
                    "Could not load restaurant data: " + e.getMessage());
            return;
        }

        RestaurantService restaurantService =
                new RestaurantService(restaurants);

        List<Restaurant> availableRestaurants =
                restaurantService.getAvailableRestaurants();

        if (availableRestaurants.isEmpty()) {
            System.out.println("No open restaurants available.");
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("Available Restaurants:");

            for (int i = 0; i < availableRestaurants.size(); i++) {
                System.out.println(
                        (i + 1) + ". "
                        + availableRestaurants.get(i).getName());
            }

            System.out.print("Choose a restaurant number: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid selection.");
                return;
            }

            int choice = scanner.nextInt();

            if (choice < 1 || choice > availableRestaurants.size()) {
                System.out.println("Invalid restaurant number.");
                return;
            }

            Restaurant selectedRestaurant =
                    availableRestaurants.get(choice - 1);

            MenuService menuService = new MenuService();

            List<MenuItem> availableItems =
                    menuService.getAvailableMenuItems(selectedRestaurant);

            System.out.println(
                    "\nMenu for " + selectedRestaurant.getName());

            if (availableItems.isEmpty()) {
                System.out.println(
                        "No menu items are currently available.");
                return;
            }

            for (MenuItem item : availableItems) {
                System.out.printf(
                        "%s - %.2f%n",
                        item.getName(),
                        item.getPrice());
            }
        }
    }
}