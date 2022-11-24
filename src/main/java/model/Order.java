package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

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
