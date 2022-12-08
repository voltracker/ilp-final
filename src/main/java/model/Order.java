package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Record representing an order retrieved from the REST Server
 * @param orderNo String containing order number
 * @param orderDate String containing order date
 * @param customer String containing customer name
 * @param creditCardNumber String containing credit card number
 * @param creditCardExpiry String containing credit card expiry date
 * @param cvv String containing credit card cvv
 * @param orderTotal int containing the order total in pence
 * @param orderItems List of Strings containing the names of the pizzas in the order
 */
public record Order(
        @JsonProperty("orderNo")
        String orderNo,
        @JsonProperty("orderDate")
        String orderDate,
        @JsonProperty("customer")
        String customer,
        @JsonProperty("creditCardNumber")
        String creditCardNumber,
        @JsonProperty("creditCardExpiry")
        String creditCardExpiry,
        @JsonProperty("cvv")
        String cvv,
        @JsonProperty("priceTotalInPence")
        int orderTotal,
        @JsonProperty("orderItems")
        List<String> orderItems
) {
}
