package najah.edu.domain;

public class ElectricVehicleDriver extends Driver {

    public ElectricVehicleDriver(String name, Status status) {
        super(name, status, DriverType.ELECTRIC_VEHICLE);
    }
}