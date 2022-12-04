package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value={"pickupRestaurant"})
public record Delivery(
        String orderNo,
        OrderOutcome outcome,
        Restaurant pickupRestaurant,
        int costInPence
) {
}
