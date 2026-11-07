package najah.edu.domain;

public class ScheduledOrder extends Order {

    public ScheduledOrder(Restaurant restaurant) {
        super(restaurant);
    }

    @Override
    public OrderType getOrderType() {
        return OrderType.SCHEDULED;
    }
}