package najah.edu.service;

import najah.edu.domain.Order;
import najah.edu.strategy.PricingStrategy;

public class PricingService {

    private final PricingStrategy pricingStrategy;

    public PricingService(PricingStrategy pricingStrategy) {

        if (pricingStrategy == null) {
            throw new IllegalArgumentException(
                    "Pricing strategy cannot be null"
            );
        }

        this.pricingStrategy = pricingStrategy;
    }

    public double calculateFinalPrice(Order order) {

        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null");
        }

        double calculatedPrice =
                pricingStrategy.calculatePrice(
                        order.getBaseFinalBill());

        return Math.round(calculatedPrice * 100.0) / 100.0;
    }
}