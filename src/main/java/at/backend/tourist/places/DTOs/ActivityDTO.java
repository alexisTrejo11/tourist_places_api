package at.backend.tourist.places.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ActivityDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("duration")
    private String duration;

    @JsonProperty("tourist_place_id")
    private Long touristPlaceId;
}
