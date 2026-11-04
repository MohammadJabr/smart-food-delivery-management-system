package najah.edu.strategy;

public class PromotionalPricingStrategy implements PricingStrategy {

    private final double discountRate;

    public PromotionalPricingStrategy(double discountRate) {

        if (!Double.isFinite(discountRate)
                || discountRate < 0
                || discountRate > 1) {

            throw new IllegalArgumentException(
                    "Discount rate must be between 0 and 1"
            );
        }

        this.discountRate = discountRate;
    }

    @Override
    public double calculatePrice(double basePrice) {

        return basePrice * (1 - discountRate);
    }
}