package najah.edu.domain;

public class DeliveryCostCalculator {

    private static final double COST_PER_KM = 2.0;

    public double calculateDeliveryCost(double distance) {

        if (distance <= 0) {
            throw new IllegalArgumentException(
                    "Distance must be greater than zero"
            );
        }

        return distance * COST_PER_KM;
    }
}