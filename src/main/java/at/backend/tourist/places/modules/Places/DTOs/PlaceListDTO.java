package at.backend.tourist.places.modules.Places.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "Data transfer object representing a list of places.")
public class PlaceListDTO {

    @Schema(description = "Unique identifier for the place list.", example = "1")
    private Long id;

    @JsonProperty("user_id")
    @Schema(description = "ID of the user who owns the place list.", example = "101")
    private Long userId;

    @Schema(description = "Name of the place list.", example = "Favorite Destinations")
    private String name;

    @Schema(description = "List of tourist places included in the place list.")
    private List<TouristPlaceDTO> places;
}
