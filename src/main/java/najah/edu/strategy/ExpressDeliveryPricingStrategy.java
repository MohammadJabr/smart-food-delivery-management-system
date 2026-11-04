package najah.edu.strategy;

public class ExpressDeliveryPricingStrategy implements PricingStrategy {

    private final double expressFee;

    public ExpressDeliveryPricingStrategy(double expressFee) {

        if (!Double.isFinite(expressFee) || expressFee < 0) {
            throw new IllegalArgumentException(
                    "Express fee must be non-negative and finite"
            );
        }

        this.expressFee = expressFee;
    }

    @Override
    public double calculatePrice(double basePrice) {
        return basePrice + expressFee;
    }
}