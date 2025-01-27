package at.backend.tourist.places.modules.Review.DTOs;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ReviewUpdateDTO {

    @Positive(message = "rating can't be negative")
    private Integer rating;

    @Schema(description = "Review Identifier", example = "4")
    @JsonProperty("review_id")
    @NotNull(message = "review_id can't be null")
    @Positive(message = "review_id can't be negative")
    private Long reviewId;

    @Schema(description = "Comment provided by the author", example = "Amazing place, highly recommend it!")
    @JsonProperty("comment")
    @NotNull(message = "comment can't be null")
    private String comment = "";

}
