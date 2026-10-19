package najah.edu.domain;

/**
 * Represents a food item offered by a restaurant.
 * Stores the item's name, price, and availability status.
 */
public class MenuItem {

    private final String name;

    private final double price;

    private boolean available;

    /**
     * Creates a menu item after validating its name and price.
     *
     * @param name the name of the food item
     * @param price the price of the food item
     * @param available whether the item is available
     * @throws IllegalArgumentException if the name is empty
     *         or the price is negative or not finite
     */
    public MenuItem(String name, double price, boolean available) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Menu item name cannot be empty");
        }

        if (!Double.isFinite(price) || price < 0) {
            throw new IllegalArgumentException(
                    "Menu item price must be finite and non-negative");
        }

        this.name = name.trim();
        this.price = price;
        this.available = available;
    }

    /**
     * Returns the name of the food item.
     *
     * @return the item name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the price of the food item.
     *
     * @return the item price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Checks whether the food item is available.
     *
     * @return true if the item is available; false otherwise
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Updates the availability status of the food item.
     *
     * @param available the new availability status
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }
}