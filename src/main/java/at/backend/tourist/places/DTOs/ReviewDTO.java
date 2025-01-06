package at.backend.tourist.places.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReviewDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("rating")
    private Integer rating;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("author")
    private String author;

    @JsonProperty("place_id")
    private Long placeId;
}
