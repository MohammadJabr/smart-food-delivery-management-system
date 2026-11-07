
package najah.edu.domain;

public class ElectricVehicleDriver extends Driver {

    private double batteryRangeKm;

    public ElectricVehicleDriver(String name, Status status) {
        this(name, status, Double.MAX_VALUE,
                Integer.MAX_VALUE, Double.MAX_VALUE);
    }

    public ElectricVehicleDriver(
            String name,
            Status status,
            double batteryRangeKm) {

        this(name, status, Double.MAX_VALUE,
                Integer.MAX_VALUE, batteryRangeKm);
    }

    public ElectricVehicleDriver(
            String name,
            Status status,
            double distanceToRestaurantKm,
            int estimatedArrivalMinutes,
            double batteryRangeKm) {

        super(name, status, distanceToRestaurantKm,
                estimatedArrivalMinutes, DriverType.ELECTRIC_VEHICLE);

        if (!Double.isFinite(batteryRangeKm) || batteryRangeKm <= 0) {
            throw new IllegalArgumentException(
                    "Battery range must be greater than zero");
        }

        this.batteryRangeKm = batteryRangeKm;
    }

    @Override
    public boolean canDeliverDistance(double distanceKm) {
        super.canDeliverDistance(distanceKm);
        return distanceKm <= batteryRangeKm;
    }

    public double getBatteryRangeKm() {
        return batteryRangeKm;
    }
}
