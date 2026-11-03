package najah.edu.domain;

public class Driver {

    public enum Status {
        AVAILABLE,
        BUSY
    }

    private final String name;
    private Status status;
    private final double distanceToRestaurantKm;
    private final int estimatedArrivalMinutes;

    public Driver(String name, Status status) {
        this(name, status, Double.MAX_VALUE, Integer.MAX_VALUE);
    }

    public Driver(String name, Status status,
                  double distanceToRestaurantKm,
                  int estimatedArrivalMinutes) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Driver name cannot be empty"
            );
        }

        if (status == null) {
            throw new IllegalArgumentException(
                    "Driver status cannot be null"
            );
        }

        if (!Double.isFinite(distanceToRestaurantKm)
                || distanceToRestaurantKm < 0) {
            throw new IllegalArgumentException(
                    "Invalid driver distance"
            );
        }

        if (estimatedArrivalMinutes < 0) {
            throw new IllegalArgumentException(
                    "Invalid arrival time"
            );
        }

        this.name = name.trim();
        this.status = status;
        this.distanceToRestaurantKm = distanceToRestaurantKm;
        this.estimatedArrivalMinutes = estimatedArrivalMinutes;
    }

    public String getName() {
        return name;
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