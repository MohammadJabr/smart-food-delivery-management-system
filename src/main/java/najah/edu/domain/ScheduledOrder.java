package najah.edu.domain;

import java.time.LocalDateTime;

public class ScheduledOrder extends Order {

    private LocalDateTime scheduledDeliveryTime;

    public ScheduledOrder(Restaurant restaurant) {
        super(restaurant);
    }

    @Override
    public OrderType getOrderType() {
        return OrderType.SCHEDULED;
    }

    public void setScheduledDeliveryTime(LocalDateTime scheduledDeliveryTime) {

        if (scheduledDeliveryTime == null) {
            throw new IllegalArgumentException(
                    "Scheduled delivery time cannot be null"
            );
        }

        if (!scheduledDeliveryTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Scheduled delivery time must be in the future"
            );
        }

        this.scheduledDeliveryTime = scheduledDeliveryTime;
    }

    public LocalDateTime getScheduledDeliveryTime() {
        return scheduledDeliveryTime;
    }
}