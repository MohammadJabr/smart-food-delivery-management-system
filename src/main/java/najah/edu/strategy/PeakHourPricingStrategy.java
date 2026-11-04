package najah.edu.strategy;

public class PeakHourPricingStrategy implements PricingStrategy {

    private final double increaseRate;

    public PeakHourPricingStrategy(double increaseRate) {

        if (!Double.isFinite(increaseRate) || increaseRate < 0) {
            throw new IllegalArgumentException(
                    "Increase rate must be non-negative and finite"
            );
        }

        this.increaseRate = increaseRate;
    }

    @Override
    public double calculatePrice(double basePrice) {

        return basePrice * (1 + increaseRate);
    }
}