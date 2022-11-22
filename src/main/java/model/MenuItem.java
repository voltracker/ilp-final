package model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * record to represent an item on a restaurant's menu
 * @param name name of pizza
 * @param price price of pizza in pence
 */
public record MenuItem(
        @JsonProperty("name")
        String name,
        @JsonProperty("priceInPence")
        int price
) {
}
