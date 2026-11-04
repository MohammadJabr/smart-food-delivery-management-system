package najah.edu.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import najah.edu.domain.MenuItem;
import najah.edu.domain.Order;
import najah.edu.domain.Restaurant;
import najah.edu.strategy.NormalPricingStrategy;
import najah.edu.strategy.PeakHourPricingStrategy;
import najah.edu.strategy.ExpressDeliveryPricingStrategy;
import najah.edu.strategy.PromotionalPricingStrategy;

public class PricingServiceTest {

    @Test
    public void testNormalPricing() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(10.0);

        MenuItem pizza =
                new MenuItem("Pizza", 20.0, true);

        restaurant.addMenuItem(pizza);

        Order order = new Order(restaurant);

        order.addItem(pizza, 2);
        order.setDeliveryAddress("Nablus");
        order.setDeliveryDistance(5.0);

        PricingService pricingService =
                new PricingService(new NormalPricingStrategy());

        assertEquals(
                50.0,
                pricingService.calculateFinalPrice(order),
                0.001
        );
    }

    @Test
    public void testPeakHourPricing() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(10.0);

        MenuItem pizza =
                new MenuItem("Pizza", 20.0, true);

        restaurant.addMenuItem(pizza);

        Order order = new Order(restaurant);

        order.addItem(pizza, 2);
        order.setDeliveryAddress("Nablus");
        order.setDeliveryDistance(5.0);

        PricingService pricingService =
                new PricingService(
                        new PeakHourPricingStrategy(0.20)
                );

        assertEquals(
                60.0,
                pricingService.calculateFinalPrice(order),
                0.001
        );
    }

    @Test
    public void testExpressDeliveryPricing() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(10.0);

        MenuItem pizza =
                new MenuItem("Pizza", 20.0, true);

        restaurant.addMenuItem(pizza);

        Order order = new Order(restaurant);

        order.addItem(pizza, 2);
        order.setDeliveryAddress("Nablus");
        order.setDeliveryDistance(5.0);

        PricingService pricingService =
                new PricingService(
                        new ExpressDeliveryPricingStrategy(8.0)
                );

        assertEquals(
                58.0,
                pricingService.calculateFinalPrice(order),
                0.001
        );
    }

    @Test
    public void testPromotionalPricing() {

        Restaurant restaurant =
                new Restaurant("Pizza House", true);

        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(10.0);

        MenuItem pizza =
                new MenuItem("Pizza", 20.0, true);

        restaurant.addMenuItem(pizza);

        Order order = new Order(restaurant);

        order.addItem(pizza, 2);
        order.setDeliveryAddress("Nablus");
        order.setDeliveryDistance(5.0);

        PricingService pricingService =
                new PricingService(
                        new PromotionalPricingStrategy(0.20)
                );

        assertEquals(
                40.0,
                pricingService.calculateFinalPrice(order),
                0.001
        );
    }
}