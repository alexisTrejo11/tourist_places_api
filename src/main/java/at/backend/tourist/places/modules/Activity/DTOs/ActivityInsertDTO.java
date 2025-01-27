package at.backend.tourist.places.modules.Activity.DTOs;

import at.backend.tourist.places.modules.Places.TouristPlace;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ActivityInsertDTO {

    @Schema(description = "Name of the activity", example = "Hiking Tour")
    @JsonProperty("name")
    @NotNull(message = "name can't be null")
    @NotBlank(message = "name can't be blank")
    private String name;

    @Schema(description = "Description of the activity", example = "A thrilling hike through the mountains.")
    @JsonProperty("description")
    private String description = "";

    @Schema(description = "Price of the activity", example = "25.50")
    @JsonProperty("price")
    @NotNull(message = "price can't be null")
    @Positive(message = "price must be greater than zero")
    private Double price;

    @Schema(description = "Duration of the activity in hours", example = "3h")
    @JsonProperty("duration")
    @NotNull(message = "duration can't be null")
    @NotBlank(message = "duration can't be blank")
    private String duration;

    @Schema(description = "ID of the related tourist place", example = "101")
    @JsonProperty("tourist_place_id")
    @NotNull(message = "tourist_place_id can't be null")
    @Positive(message = "tourist_place_id must be greater than zero")
    private Long touristPlaceId;

    @Schema(hidden = true)
    private TouristPlace touristPlace;
}
