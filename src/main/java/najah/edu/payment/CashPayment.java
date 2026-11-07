package najah.edu.payment;

import najah.edu.domain.Order;

public class CashPayment implements PaymentMethod {

    private boolean paid;

    public CashPayment() {
        this.paid = false;
    }

    @Override
    public void processPayment(Order order, double amount) {

        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null"
            );
        }

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero"
            );
        }

        double requiredAmount = order.getFinalBill();

        if (amount < requiredAmount) {
            throw new IllegalArgumentException(
                    "Payment amount is less than the required amount"
            );
        }

        if (amount > requiredAmount) {
            throw new IllegalArgumentException(
                    "Payment amount is greater than the required amount"
            );
        }

        paid = true;
    }

    @Override
    public boolean isPaid() {
        return paid;
    }
}