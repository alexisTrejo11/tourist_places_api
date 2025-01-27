package at.backend.tourist.places.modules.Places.DTOs;

import at.backend.tourist.places.core.Utils.PlaceRelationships;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "DTO used for inserting a new tourist place")
public class TouristPlaceInsertDTO {

    @JsonProperty("name")
    @NotNull(message = "name can't be null")
    @NotBlank(message = "name can't be blank")
    @Schema(description = "Name of the tourist place", example = "Eiffel Tower", required = true)
    private String name;

    @JsonProperty("description")
    @NotNull(message = "description can't be null")
    @Schema(description = "Brief description of the tourist place", example = "A global cultural icon of France and one of the most recognizable structures in the world.", required = true)
    private String description;

    @JsonProperty("image")
    @Schema(description = "URL of an image representing the tourist place", example = "https://example.com/images/eiffel_tower.jpg")
    private String image;

    @JsonProperty("opening_hours")
    @NotNull(message = "opening_hours can't be null")
    @NotBlank(message = "opening_hours can't be blank")
    @Schema(description = "Opening hours of the tourist place", example = "09:00 - 23:00", required = true)
    private String openingHours;

    @JsonProperty("price_range")
    @NotNull(message = "price_range can't be null")
    @NotBlank(message = "price_range can't be blank")
    @Schema(description = "Price range for visiting the tourist place", example = "$$", required = true)
    private String priceRange;

    @JsonProperty("country_id")
    @NotNull(message = "country_id can't be null")
    @Positive(message = "country_id can't be negative")
    @Schema(description = "Unique identifier of the country where the place is located", example = "1", required = true)
    private Long countryId;

    @JsonProperty("category_id")
    @NotNull(message = "category_id can't be null")
    @Positive(message = "category_id can't be negative")
    @Schema(description = "Unique identifier of the category of the tourist place", example = "3", required = true)
    private Long categoryId;

    @Schema(description = "Relationships to other places, if applicable")
    private PlaceRelationships placeRelationships;
}
