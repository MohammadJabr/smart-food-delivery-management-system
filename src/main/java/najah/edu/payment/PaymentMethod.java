package najah.edu.payment;

import najah.edu.domain.Order;

public interface PaymentMethod {

    void processPayment(Order order, double amount);

    boolean isPaid();
}