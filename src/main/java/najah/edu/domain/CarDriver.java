package najah.edu.domain;

public class CarDriver extends Driver {

    public CarDriver(String name, Status status) {
        super(name, status, DriverType.CAR);
    }
}