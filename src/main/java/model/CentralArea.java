package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value={"name"})
public record CentralArea(
        @JsonProperty("longitude")
        double lng,
        @JsonProperty("latitude")
        double lat) {
}
