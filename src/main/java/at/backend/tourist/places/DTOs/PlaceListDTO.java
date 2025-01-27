package at.backend.tourist.places.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlaceListDTO {
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    private String name;

    private List<TouristPlaceDTO> places;

}