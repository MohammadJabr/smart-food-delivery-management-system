package najah.edu.domain;

public class CarDriver extends Driver {

    private static final int MAX_ORDER_QUANTITY = 20;

    public CarDriver(String name, Status status) {
        super(name, status, DriverType.CAR);
    }

    public boolean canHandleOrderQuantity(int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Order quantity must be greater than zero"
            );
        }

        return quantity <= MAX_ORDER_QUANTITY;
    }
}