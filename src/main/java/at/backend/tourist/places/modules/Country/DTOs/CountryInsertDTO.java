package at.backend.tourist.places.modules.Country.DTOs;

import at.backend.tourist.places.core.Utils.Enum.Continent;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountryInsertDTO {

    @Schema(description = "Name of the country", example = "Japan", required = true)
    @JsonProperty("name")
    @NotNull(message = "name can't be null")
    @NotBlank(message = "name can't be blank")
    private String name;

    @Schema(description = "Capital city of the country", example = "Tokyo", required = true)
    @JsonProperty("capital")
    @NotNull(message = "capital can't be null")
    @NotBlank(message = "capital can't be blank")
    private String capital;

    @Schema(description = "Currency of the country", example = "Yen", required = true)
    @JsonProperty("currency")
    @NotNull(message = "currency can't be null")
    @NotBlank(message = "currency can't be blank")
    private String currency;

    @Schema(description = "Official language of the country", example = "Japanese", required = true)
    @JsonProperty("language")
    @NotNull(message = "language can't be null")
    @NotBlank(message = "language can't be blank")
    private String language;

    @Schema(description = "Population of the country", example = "125800000", required = true)
    @JsonProperty("population")
    @NotNull(message = "population can't be null")
    @Positive(message = "population must be positive")
    private Long population;

    @Schema(description = "Area of the country in square kilometers", example = "377975.0", required = true)
    @JsonProperty("area")
    @NotNull(message = "area can't be null")
    @Positive(message = "area must be positive")
    private Double area;

    @Schema(description = "Continent where the country is located", example = "ASIA", required = true)
    @JsonProperty("continent")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "continent can't be null")
    private Continent continent;

    @Schema(description = "URL of the country's flag image", example = "https://example.com/flags/japan.png")
    @JsonProperty("flag_image")
    private String flagImage;
}
