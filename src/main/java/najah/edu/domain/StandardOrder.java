package najah.edu.domain;

public class StandardOrder extends Order {

    public StandardOrder(Restaurant restaurant) {
        super(restaurant);
    }

    @Override
    public OrderType getOrderType() {
        return OrderType.STANDARD;
    }
}