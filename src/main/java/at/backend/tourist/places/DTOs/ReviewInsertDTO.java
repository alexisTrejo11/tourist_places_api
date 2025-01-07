package at.backend.tourist.places.DTOs;

import at.backend.tourist.places.Models.TouristPlace;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ReviewInsertDTO {
    @JsonProperty("rating")
    @NotNull(message = "rating can't be null")
    @Positive(message = "rating can't be negative")
    private Integer rating;

    @JsonProperty("comment")
    @NotNull(message = "comment can't be null")
    private String comment = "";

    @JsonProperty("author")
    @NotNull(message = "author can't be null")
    @NotBlank(message = "author can't be blank")
    private String author = "";

    @JsonProperty("place_id")
    @NotNull(message = "place_id can't be null")
    @Positive(message = "place_id can't be negative")
    private Long placeId;

    private TouristPlace touristPlace;
}
