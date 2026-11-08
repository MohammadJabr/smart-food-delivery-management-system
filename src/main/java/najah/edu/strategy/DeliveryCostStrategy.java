package najah.edu.strategy;

/**
 * Strategy interface for calculating delivery costs.
 */
public interface DeliveryCostStrategy {

    /**
     * Calculates a delivery fee for the supplied distance.
     *
     * @param distanceKm delivery distance in kilometers
     * @return calculated delivery fee
     */
    double calculateCost(double distanceKm);
}
