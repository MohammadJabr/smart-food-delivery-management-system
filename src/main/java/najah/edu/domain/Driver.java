package najah.edu.domain;

public class Driver {

    public enum Status {
        AVAILABLE,
        BUSY
    }

    private final String name;
    private final DriverType driverType;
    private Status status;
    private final double distanceToRestaurantKm;
    private final int estimatedArrivalMinutes;

    public Driver(String name, Status status) {
        this(name, status, Double.MAX_VALUE, Integer.MAX_VALUE,
                DriverType.UNSPECIFIED);
    }

    public Driver(String name, Status status,
                  double distanceToRestaurantKm,
                  int estimatedArrivalMinutes) {
        this(name, status, distanceToRestaurantKm,
                estimatedArrivalMinutes, DriverType.UNSPECIFIED);
    }

    protected Driver(String name, Status status, DriverType driverType) {
        this(name, status, Double.MAX_VALUE, Integer.MAX_VALUE, driverType);
    }

    protected Driver(String name, Status status,
                     double distanceToRestaurantKm,
                     int estimatedArrivalMinutes,
                     DriverType driverType) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Driver name cannot be empty");
        }

        if (status == null) {
            throw new IllegalArgumentException("Driver status cannot be null");
        }

        if (!Double.isFinite(distanceToRestaurantKm)
                || distanceToRestaurantKm < 0) {
            throw new IllegalArgumentException("Invalid driver distance");
        }

        if (estimatedArrivalMinutes < 0) {
            throw new IllegalArgumentException("Invalid arrival time");
        }

        if (driverType == null) {
            throw new IllegalArgumentException("Driver type cannot be null");
        }

        this.name = name.trim();
        this.status = status;
        this.distanceToRestaurantKm = distanceToRestaurantKm;
        this.estimatedArrivalMinutes = estimatedArrivalMinutes;
        this.driverType = driverType;
    }

    public String getName() {
        return name;
    }

    public DriverType getDriverType() {
        return driverType;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isAvailable() {
        return status == Status.AVAILABLE;
    }

    public double getDistanceToRestaurantKm() {
        return distanceToRestaurantKm;
    }

    public int getEstimatedArrivalMinutes() {
        return estimatedArrivalMinutes;
    }

    void markBusy() {
        status = Status.BUSY;
    }
}