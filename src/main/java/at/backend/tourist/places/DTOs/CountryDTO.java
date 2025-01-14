package at.backend.tourist.places.DTOs;

import at.backend.tourist.places.Utils.Enum.Continent;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("capital")
    private String capital;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("language")
    private String language;

    @JsonProperty("population")
    private Long population;

    @JsonProperty("area")
    private Double area;

    @JsonProperty("continent")
    @Enumerated(EnumType.STRING)
    private Continent continent;

    @JsonProperty("flag_image")
    private String flagImage;
}
