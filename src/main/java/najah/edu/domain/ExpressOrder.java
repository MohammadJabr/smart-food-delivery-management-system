package najah.edu.domain;

public class ExpressOrder extends Order {

    private static final double EXPRESS_FEE = 10.0;

    public ExpressOrder(Restaurant restaurant) {
        super(restaurant);
    }

    @Override
    public OrderType getOrderType() {
        return OrderType.EXPRESS;
    }

    @Override
    public double getFinalBill() {
        return super.getFinalBill() + EXPRESS_FEE;
    }

    public double getExpressFee() {
        return EXPRESS_FEE;
    }
}