package najah.edu.service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import najah.edu.domain.Order;
import najah.edu.payment.CashPayment;
import najah.edu.payment.PaymentMethod;

public class PaymentService {

    private final Map<Order, PaymentMethod> payments =
            new HashMap<>();

    private final Supplier<PaymentMethod> paymentMethodFactory;

    public PaymentService() {
        this(CashPayment::new);
    }

    public PaymentService(Supplier<PaymentMethod> paymentMethodFactory) {
        if (paymentMethodFactory == null) {
            throw new IllegalArgumentException(
                    "Payment method factory cannot be null");
        }

        this.paymentMethodFactory = paymentMethodFactory;
    }

    public void processCashPayment(Order order, double amount) {

        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null");
        }

        PaymentMethod payment = payments.get(order);

        if (payment == null) {
            payment = paymentMethodFactory.get();

            if (payment == null) {
                throw new IllegalStateException(
                        "Payment method factory returned null");
            }

            payments.put(order, payment);
        }

        if (payment.isPaid()) {
            throw new IllegalStateException(
                    "Order has already been paid");
        }

        payment.processPayment(order, amount);
    }

    public boolean isPaid(Order order) {

        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null");
        }

        PaymentMethod payment = payments.get(order);

        return payment != null && payment.isPaid();
    }

    public String getPaymentStatus(Order order) {
        return isPaid(order) ? "PAID" : "UNPAID";
    }
}