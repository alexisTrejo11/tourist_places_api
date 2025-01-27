package at.backend.tourist.places.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlaceListInsertDTO {
    @NotNull(message = "name can't be null")
    @NotBlank(message = "name can't be blank")
    @Size(min = 3, message = "name must has at least 3 character")
    private String name;

    @JsonProperty("places_ids")
    private Set<Long> placesIds =  new HashSet<>();

    @JsonProperty("user_id")
    private Long userId;
}