package najah.edu.domain;

import najah.edu.strategy.DeliveryCostStrategy;
import najah.edu.strategy.DistanceBasedDeliveryCostStrategy;

public class DeliveryCostCalculator {

    private final DeliveryCostStrategy strategy;

    public DeliveryCostCalculator() {
        this(new DistanceBasedDeliveryCostStrategy());
    }

    public DeliveryCostCalculator(DeliveryCostStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException(
                    "Delivery cost strategy cannot be null");
        }

        this.strategy = strategy;
    }

    public double calculateDeliveryCost(double distance) {

        if (!Double.isFinite(distance) || distance <= 0) {
            throw new IllegalArgumentException(
                    "Distance must be greater than zero and finite");
        }

        double cost = strategy.calculateCost(distance);

        if (!Double.isFinite(cost) || cost < 0) {
            throw new IllegalArgumentException(
                    "Delivery cost must be non-negative and finite");
        }

        return cost;
    }
}
