package at.backend.tourist.places.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlaceListInsertDTO {
    @NotNull(message = "name can't be null")
    @NotBlank(message = "name can't be blank")
    @Min(value = 3, message = "name must has at least 3 character")
    private String name;

    @JsonProperty("places_ids")
    @NotNull(message = "places_ids can't be null")
    @NotEmpty(message = "places_ids can't be empty")
    private Set<Long> placesIds;

    @JsonProperty("user_id")
    @NotNull(message = "user_id can't be null")
    @Positive(message = "user_id can't be negative")
    private Long userId;
}