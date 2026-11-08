package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import najah.edu.strategy.DeliveryCostStrategy;

public class DeliveryCostTest {

    private Order order;

    @BeforeEach
    public void setUp() {

        Restaurant restaurant =
                new Restaurant("Hulk Burger", true);

        restaurant.setDeliveryZone("Nablus");
        restaurant.setMinimumOrderValue(10.0);

        MenuItem burger =
                new MenuItem("Hulk Burger Meal", 20.0, true);

        restaurant.addMenuItem(burger);

        order = new Order(restaurant);
        order.addItem(burger, 2);
        order.setDeliveryAddress("Nablus");
    }

    @Test
    public void testDeliveryCostBasedOnDistance() {

        order.setDeliveryDistance(5.0);

        assertEquals(
                10.0,
                order.getDeliveryFee(),
                0.001
        );
    }

    @Test
    public void testDeliveryFeeIncludedInFinalBill() {

        order.setDeliveryDistance(5.0);

        assertEquals(
                50.0,
                order.getFinalBill(),
                0.001
        );
    }

    @Test
    public void testZeroDistanceIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> order.setDeliveryDistance(0)
        );
    }

    @Test
    public void testNegativeDistanceIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> order.setDeliveryDistance(-5)
        );
    }
    @Test
    public void testInjectedDeliveryCostStrategyIsUsed() {

        DeliveryCostStrategy strategy =
                mock(DeliveryCostStrategy.class);

        when(strategy.calculateCost(5.0)).thenReturn(17.5);

        Order strategyOrder =
                new Order(order.getRestaurant(), strategy);

        strategyOrder.setDeliveryDistance(5.0);

        assertEquals(17.5, strategyOrder.getDeliveryFee(), 0.001);
        verify(strategy).calculateCost(5.0);
    }

    @Test
    public void testNonFiniteDistanceIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> order.setDeliveryDistance(Double.NaN)
        );
    }

}