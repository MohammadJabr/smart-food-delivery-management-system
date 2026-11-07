
package najah.edu.domain;

public class MotorcycleDriver extends Driver {

    private static final double MAX_DELIVERY_DISTANCE_KM = 15.0;

    public MotorcycleDriver(String name, Status status) {
        super(name, status, DriverType.MOTORCYCLE);
    }

    public MotorcycleDriver(
            String name,
            Status status,
            double distanceToRestaurantKm,
            int estimatedArrivalMinutes) {

        super(name, status, distanceToRestaurantKm,
                estimatedArrivalMinutes, DriverType.MOTORCYCLE);
    }

    @Override
    public boolean canDeliverDistance(double distanceKm) {
        super.canDeliverDistance(distanceKm);
        return distanceKm <= MAX_DELIVERY_DISTANCE_KM;
    }
}
