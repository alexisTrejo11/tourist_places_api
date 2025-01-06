package at.backend.tourist.places.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TouristPlaceDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("rating")
    private Double rating;

    @JsonProperty("image")
    private String image;

    @JsonProperty("opening_hours")
    private String openingHours;

    @JsonProperty("price_range")
    private String priceRange;

    @JsonProperty("country_id")
    private Long countryId;

    @JsonProperty("category_id")
    private Long categoryId;
}
