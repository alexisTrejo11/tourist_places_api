package at.backend.tourist.places.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO representing a tourist place")
public class TouristPlaceDTO {

    @JsonProperty("id")
    @Schema(description = "Unique identifier of the tourist place", example = "1")
    private Long id;

    @JsonProperty("name")
    @Schema(description = "Name of the tourist place", example = "Iguazu Falls")
    private String name;

    @JsonProperty("description")
    @Schema(description = "Short description of the tourist place", example = "One of the most impressive waterfalls in the world.")
    private String description;

    @JsonProperty("rating")
    @Schema(description = "Average rating of the tourist place", example = "4.8")
    private Double rating;

    @JsonProperty("image")
    @Schema(description = "URL of the representative image of the place", example = "https://example.com/images/iguazu.jpg")
    private String image;

    @JsonProperty("opening_hours")
    @Schema(description = "Opening hours of the tourist place", example = "08:00 - 18:00")
    private String openingHours;

    @JsonProperty("price_range")
    @Schema(description = "Price range to visit the place", example = "$$$")
    private String priceRange;

    @JsonProperty("country_id")
    @Schema(description = "Unique identifier of the country where the place is located", example = "10")
    private Long countryId;

    @JsonProperty("category_id")
    @Schema(description = "Unique identifier of the category of the tourist place", example = "2")
    private Long categoryId;
}
