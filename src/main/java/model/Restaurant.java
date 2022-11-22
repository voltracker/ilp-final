package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Record to represent a restaurant
 * @param name name of restaurant
 * @param lng longitude
 * @param lat latitude
 * @param menu list of MenuItems containing pizzas
 */
public record Restaurant(
        @JsonProperty("name")
        String name,
        @JsonProperty("longitude")
        double lng,
        @JsonProperty("latitude")
        double lat,
        @JsonProperty("menu")
        List<MenuItem> menu
){

}
