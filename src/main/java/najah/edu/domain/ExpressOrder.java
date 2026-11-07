package najah.edu.domain;

public class ExpressOrder extends Order {

    public ExpressOrder(Restaurant restaurant) {
        super(restaurant);
    }

    @Override
    public OrderType getOrderType() {
        return OrderType.EXPRESS;
    }
}