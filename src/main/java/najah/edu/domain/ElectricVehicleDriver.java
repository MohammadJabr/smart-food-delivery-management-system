package najah.edu.domain;

public class ElectricVehicleDriver extends Driver {

    private double batteryRangeKm;

    public ElectricVehicleDriver(String name, Status status) {
        super(name, status, DriverType.ELECTRIC_VEHICLE);
        this.batteryRangeKm = Double.MAX_VALUE;
    }

    public ElectricVehicleDriver(
            String name,
            Status status,
            double batteryRangeKm) {

        super(name, status, DriverType.ELECTRIC_VEHICLE);

        if (batteryRangeKm <= 0) {
            throw new IllegalArgumentException(
                    "Battery range must be greater than zero"
            );
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