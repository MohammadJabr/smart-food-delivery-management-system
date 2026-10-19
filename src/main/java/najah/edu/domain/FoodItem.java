package najah.edu.domain;

public class FoodItem {

    private String name;
    private double price;

    public FoodItem(String name, double price) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Food item name cannot be empty"
            );
        }

        if (price < 0) {
            throw new IllegalArgumentException(
                    "Food item price cannot be negative"
            );
        }

        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}