package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Record used for deserializing REST Server response
 * @param lng double containing longitude
 * @param lat double containing latitude
 */
@JsonIgnoreProperties(value={"name"})
public record CentralArea(
        @JsonProperty("longitude")
        double lng,
        @JsonProperty("latitude")
        double lat) {
}
