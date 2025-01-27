package at.backend.tourist.places.modules.Places.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO for inserting a new place category")
public class PlaceCategoryInsertDTO {

    @JsonProperty("name")
    @NotNull(message = "name can't be null")
    @NotBlank(message = "name can't be blank")
    @Schema(description = "Name of the category", example = "Beaches")
    private String name;

    @JsonProperty("description")
    @NotNull(message = "description can't be null")
    @Schema(description = "Description of the category", example = "Scenic seaside locations")
    private String description;
}
