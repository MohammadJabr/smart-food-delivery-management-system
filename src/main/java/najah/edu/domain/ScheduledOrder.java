package najah.edu.domain;

import java.time.LocalDateTime;

import najah.edu.service.DateTimeService;

public class ScheduledOrder extends Order {

    private LocalDateTime scheduledDeliveryTime;
    private DateTimeService dateTimeService;

    public ScheduledOrder(Restaurant restaurant) {
        super(restaurant);
        this.dateTimeService = new DateTimeService();
    }

    public ScheduledOrder(
            Restaurant restaurant,
            DateTimeService dateTimeService) {

        super(restaurant);

        if (dateTimeService == null) {
            throw new IllegalArgumentException(
                    "DateTimeService cannot be null"
            );
        }

        this.dateTimeService = dateTimeService;
    }

    @Override
    public OrderType getOrderType() {
        return OrderType.SCHEDULED;
    }

    public void setScheduledDeliveryTime(
            LocalDateTime scheduledDeliveryTime) {

        if (scheduledDeliveryTime == null) {
            throw new IllegalArgumentException(
                    "Scheduled delivery time cannot be null"
            );
        }

        if (!scheduledDeliveryTime.isAfter(
                dateTimeService.getCurrentTime())) {

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