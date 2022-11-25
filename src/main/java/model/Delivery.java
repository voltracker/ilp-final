package model;

public record Delivery(
        String orderNo,
        OrderOutcome outcome,
        Restaurant pickupRestaurant,
        int costInPence
) {
}
