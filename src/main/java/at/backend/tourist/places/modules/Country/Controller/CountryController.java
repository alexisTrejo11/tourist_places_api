package at.backend.tourist.places.modules.Country.Controller;

import at.backend.tourist.places.core.SwaggerHelper.ApiResponseExamples;
import at.backend.tourist.places.modules.Country.DTOs.CountryDTO;
import at.backend.tourist.places.modules.Country.DTOs.CountryInsertDTO;
import at.backend.tourist.places.core.Utils.Enum.Continent;
import at.backend.tourist.places.core.Utils.Response.ResponseWrapper;
import at.backend.tourist.places.modules.Country.Service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/countries")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @Operation(summary = "Get a country by its ID", description = "Retrieve detailed information about a country by its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Country found",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.USER))),
            @ApiResponse(responseCode = "404", description = "Country not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NOT_FOUND)))
    })
    @GetMapping("/{id}")
    public ResponseWrapper<CountryDTO> getCountryById(
            @Parameter(description = "ID of the country", example = "1") @PathVariable Long id) {
        CountryDTO country = countryService.getById(id);
        return ResponseWrapper.found(country, "Country");
    }

    @Operation(summary = "Get a country by its name", description = "Retrieve detailed information about a country by its name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Country found", content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.COUNTRY))),
            @ApiResponse(responseCode = "404", description = "Country not found", content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NOT_FOUND)))
    })
    @GetMapping("/name/{name}")
    public ResponseWrapper<CountryDTO> getCountryByName(
            @Parameter(description = "Name of the country", example = "Japan") @Valid @PathVariable String name) {
        CountryDTO country = countryService.getByName(name);
        return ResponseWrapper.found(country, "Country");
    }

    @Operation(summary = "Get countries by continent", description = "Retrieve a list of countries belonging to a specific continent.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Countries found",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.COUNTRY))),
            @ApiResponse(responseCode = "400", description = "Invalid continent",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.BAD_REQUEST)))
    })
    @GetMapping("/by-continent/{continentSTR}")
    public ResponseWrapper<List<CountryDTO>> getCountryByContinent(
            @Parameter(description = "Continent name in uppercase", example = "ASIA") @Valid @PathVariable String continentSTR) {
        Continent continent = Continent.valueOf(continentSTR.toUpperCase());
        List<CountryDTO> countries = countryService.getByContinent(continent);
        return ResponseWrapper.found(countries, "Countries");
    }

    @Operation(summary = "Get all countries", description = "Retrieve a list of all countries." )
    @ApiResponse(responseCode = "200", description = "List of countries retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.COUNTRIES)))
    @GetMapping
    public ResponseWrapper<List<CountryDTO>> getAllCountries() {
        List<CountryDTO> countries = countryService.getAll();
        return ResponseWrapper.found(countries, "Countries");
    }

    @Operation(
            summary = "Create a new country",
            description = "Create a new country entry. **Requires ADMIN role**.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Country created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.COUNTRY_CREATED))),
            @ApiResponse(responseCode = "400", description = "Validation error or invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.UNAUTHORIZED_ACCESS))),
            @ApiResponse(responseCode = "403", description = "Forbidden, user lacks necessary permissions (admin required)",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.FORBIDDEN)))
    })
    @PostMapping
    public ResponseEntity<ResponseWrapper<CountryDTO>> createCountry(@Valid @RequestBody CountryInsertDTO insertDTO) {
        CountryDTO createdCountry = countryService.create(insertDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created(createdCountry, "Country"));
    }

    @Operation(
            summary = "Delete a country by its ID",
            description = "Delete an existing country. **Requires ADMIN role**.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Country deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.SUCCESS))),
            @ApiResponse(responseCode = "404", description = "Country not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NOT_FOUND))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.UNAUTHORIZED_ACCESS))),
            @ApiResponse(responseCode = "403", description = "Forbidden, user lacks necessary permissions (admin required)",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.FORBIDDEN)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Void>> deleteCountry(@Parameter(description = "ID of the country to delete", example = "1")
                                                   @PathVariable Long id) {
        countryService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ResponseWrapper.deleted("Country"));
    }
}
