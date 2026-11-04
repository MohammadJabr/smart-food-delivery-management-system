package najah.edu.strategy;

public class NormalPricingStrategy implements PricingStrategy {

    @Override
    public double calculatePrice(double basePrice) {
        return basePrice;
    }
}