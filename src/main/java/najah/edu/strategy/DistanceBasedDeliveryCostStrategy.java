package najah.edu.strategy;

/**
 * Calculates delivery fees using a fixed cost per kilometer.
 */
public class DistanceBasedDeliveryCostStrategy
        implements DeliveryCostStrategy {

    private static final double DEFAULT_COST_PER_KM = 2.0;

    private final double costPerKm;

    public DistanceBasedDeliveryCostStrategy() {
        this(DEFAULT_COST_PER_KM);
    }

    public DistanceBasedDeliveryCostStrategy(double costPerKm) {
        if (!Double.isFinite(costPerKm) || costPerKm < 0) {
            throw new IllegalArgumentException(
                    "Cost per kilometer must be non-negative and finite");
        }

        this.costPerKm = costPerKm;
    }

    @Override
    public double calculateCost(double distanceKm) {
        if (!Double.isFinite(distanceKm) || distanceKm <= 0) {
            throw new IllegalArgumentException(
                    "Distance must be greater than zero and finite");
        }

        return distanceKm * costPerKm;
    }
}
