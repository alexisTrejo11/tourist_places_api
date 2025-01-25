package at.backend.tourist.places.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object for place category")
public class PlaceCategoryDTO {

    @JsonProperty("id")
    @Schema(description = "Unique identifier of the category", example = "1")
    private Long id;

    @JsonProperty("name")
    @Schema(description = "Name of the category", example = "Historical Places")
    private String name;

    @JsonProperty("description")
    @Schema(description = "Description of the category", example = "Places with historical significance")
    private String description;
}
