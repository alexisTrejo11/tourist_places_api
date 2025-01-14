package at.backend.tourist.places.DTOs;

import at.backend.tourist.places.Utils.Enum.Continent;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CountryInsertDTO {
    @JsonProperty("name")
    @NotNull(message = "name can't be null")
    @NotBlank(message = "name can't be blank")
    private String name;

    @JsonProperty("capital")
    @NotNull(message = "capital can't be null")
    @NotBlank(message = "capital can't be blank")
    private String capital;

    @JsonProperty("currency")
    @NotNull(message = "currency can't be null")
    @NotBlank(message = "currency can't be blank")
    private String currency;

    @JsonProperty("language")
    @NotNull(message = "language can't be null")
    @NotBlank(message = "language can't be blank")
    private String language;

    @JsonProperty("population")
    @NotNull(message = "population can't be null")
    @Positive(message = "population can't be blank")
    private Long population;

    @JsonProperty("area")
    @NotNull(message = "area can't be null")
    @Positive(message = "area can't be blank")
    private Double area;

    @JsonProperty("continent")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "continent can't be null")
    private Continent continent;

    @JsonProperty("flag_image")
    private String flagImage;
}
