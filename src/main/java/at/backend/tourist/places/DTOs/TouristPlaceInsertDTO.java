package at.backend.tourist.places.DTOs;

import at.backend.tourist.places.Utils.PlaceRelationships;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TouristPlaceInsertDTO {
    @JsonProperty("name")
    @NotNull(message = "name can't be null")
    @NotBlank(message = "name can't be blank")
    private String name;

    @JsonProperty("description")
    @NotNull(message = "description can't be null")
    private String description;

    @JsonProperty("image")
    private String image;

    @JsonProperty("opening_hours")
    @NotNull(message = "opening_hours can't be null")
    @NotBlank(message = "opening_hours can't be blank")
    private String openingHours;

    @JsonProperty("price_range")
    @NotNull(message = "price_range can't be null")
    @NotBlank(message = "price_range can't be blank")
    private String priceRange;

    @JsonProperty("country_id")
    @NotNull(message = "country_id can't be null")
    @Positive(message = "country_id can't be negative")
    private Long countryId;

    @JsonProperty("category_id")
    @NotNull(message = "category_id can't be null")
    @Positive(message = "category_id can't be negative")
    private Long categoryId;

    private PlaceRelationships placeRelationships;
}
