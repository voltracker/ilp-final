package model;

public record Delivery(
        String orderNo,
        OrderOutcome outcome,
        int costInPence
) {
}
