package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Record that represents an order after it has been process
 * @param orderNo String containing the order number
 * @param outcome OrderOutcome from enum
 * @param pickupRestaurant Restaurant to which the order will be collected from
 * @param costInPence int containing the total order cost in pence
 */
@JsonIgnoreProperties(value={"pickupRestaurant"})
public record Delivery(
        String orderNo,
        OrderOutcome outcome,
        Restaurant pickupRestaurant,
        int costInPence
) {
}
