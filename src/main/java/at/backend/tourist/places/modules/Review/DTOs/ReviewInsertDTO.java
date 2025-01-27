package at.backend.tourist.places.modules.Review.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ReviewInsertDTO {

    @Schema(description = "Rating given by the author (1-5)", example = "4")
    @JsonProperty("rating")
    @NotNull(message = "rating can't be null")
    @Positive(message = "rating can't be negative")
    private Integer rating;

    @Schema(description = "Comment provided by the author", example = "Amazing place, highly recommend it!")
    @JsonProperty("comment")
    @NotNull(message = "comment can't be null")
    private String comment = "";

    @Schema(description = "Name of the review's author", example = "Jane Doe")
    @JsonProperty("author_email")
    private String authorEmail = "";

    @Schema(description = "ID of the place being reviewed", example = "101")
    @JsonProperty("place_id")
    @NotNull(message = "place_id can't be null")
    @Positive(message = "place_id can't be negative")
    private Long placeId;


}
