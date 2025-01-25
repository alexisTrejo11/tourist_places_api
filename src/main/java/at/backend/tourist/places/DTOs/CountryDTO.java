package at.backend.tourist.places.DTOs;

import at.backend.tourist.places.Utils.Enum.Continent;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDTO {

    @Schema(description = "Unique identifier of the country", example = "1")
    @JsonProperty("id")
    private Long id;

    @Schema(description = "Name of the country", example = "Japan")
    @JsonProperty("name")
    private String name;

    @Schema(description = "Capital city of the country", example = "Tokyo")
    @JsonProperty("capital")
    private String capital;

    @Schema(description = "Currency of the country", example = "Yen")
    @JsonProperty("currency")
    private String currency;

    @Schema(description = "Official language of the country", example = "Japanese")
    @JsonProperty("language")
    private String language;

    @Schema(description = "Population of the country", example = "125800000")
    @JsonProperty("population")
    private Long population;

    @Schema(description = "Area of the country in square kilometers", example = "377975.0")
    @JsonProperty("area")
    private Double area;

    @Schema(description = "Continent where the country is located", example = "ASIA")
    @JsonProperty("continent")
    @Enumerated(EnumType.STRING)
    private Continent continent;

    @Schema(description = "URL of the country's flag image", example = "https://example.com/flags/japan.png")
    @JsonProperty("flag_image")
    private String flagImage;
}
