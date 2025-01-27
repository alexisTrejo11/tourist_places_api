package at.backend.tourist.places.modules.Activity.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ActivityDTO {

    @Schema(description = "Unique identifier of the activity", example = "1")
    @JsonProperty("id")
    private Long id;

    @Schema(description = "Name of the activity", example = "City Tour")
    @JsonProperty("name")
    private String name;

    @Schema(description = "Description of the activity", example = "A guided tour through the city's landmarks.")
    @JsonProperty("description")
    private String description;

    @Schema(description = "Price of the activity", example = "49.99")
    @JsonProperty("price")
    private Double price;

    @Schema(description = "Duration of the activity in hours", example = "2h")
    @JsonProperty("duration")
    private String duration;

    @Schema(description = "ID of the related tourist place", example = "101")
    @JsonProperty("tourist_place_id")
    private Long touristPlaceId;
}
