package najah.edu.domain;

public class BicycleDriver extends Driver {

    private static final double MAX_DELIVERY_DISTANCE_KM = 5.0;

    public BicycleDriver(String name, Status status) {
        super(name, status, DriverType.BICYCLE);
    }

    @Override
    public boolean canDeliverDistance(double distanceKm) {

        super.canDeliverDistance(distanceKm);

        return distanceKm <= MAX_DELIVERY_DISTANCE_KM;
    }
}