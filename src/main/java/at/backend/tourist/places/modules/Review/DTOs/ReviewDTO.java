package at.backend.tourist.places.modules.Review.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReviewDTO {

    @Schema(description = "Unique identifier of the review", example = "1", required = true)
    @JsonProperty("id")
    private Long id;

    @Schema(description = "Rating given by the author (1-5)", example = "4", required = true)
    @JsonProperty("rating")
    private Integer rating;

    @Schema(description = "Comment provided by the author", example = "Great place to visit!", required = true)
    @JsonProperty("comment")
    private String comment;

    @Schema(description = "Name of the review's author", example = "John Doe", required = true)
    @JsonProperty("author_id")
    private String authorId;

    @Schema(description = "ID of the place being reviewed", example = "101", required = true)
    @JsonProperty("place_id")
    private Long placeId;
}
