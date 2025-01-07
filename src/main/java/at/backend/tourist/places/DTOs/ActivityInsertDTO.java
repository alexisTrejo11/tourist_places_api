package at.backend.tourist.places.DTOs;

import at.backend.tourist.places.Models.TouristPlace;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ActivityInsertDTO {
    @JsonProperty("name")
    @NotNull(message = "name can't be null")
    @NotBlank(message = "name can't be blank")
    private String name;

    @JsonProperty("description")
    private String description = "";

    @JsonProperty("price")
    @NotNull(message = "price can't be null")
    @Positive(message = "price can't be blank")
    private Double price;

    @JsonProperty("duration")
    @NotNull(message = "duration can't be null")
    @NotBlank(message = "duration can't be blank")
    private String duration;

    @JsonProperty("tourist_place_id")
    @NotNull(message = "tourist_place_id can't be null")
    @Positive(message = "tourist_place_id can't be blank")
    private Long touristPlaceId;

    private TouristPlace touristPlace;
}
