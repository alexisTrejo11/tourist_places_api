package at.backend.tourist.places.modules.Country.Controller;

import at.backend.tourist.places.modules.Country.DTOs.CountryDTO;
import at.backend.tourist.places.modules.Country.DTOs.CountryInsertDTO;
import at.backend.tourist.places.core.Utils.Enum.Continent;
import at.backend.tourist.places.core.Utils.ResponseWrapper;
import at.backend.tourist.places.modules.Country.Service.CountryService;
import at.backend.tourist.places.core.Utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/countries")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @Operation(summary = "Get a country by its ID", description = "Retrieve detailed information about a country by its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Country found"),
            @ApiResponse(responseCode = "404", description = "Country not found")
    })
    @GetMapping("/{id}")
    public ResponseWrapper<CountryDTO> getCountryById(
            @Parameter(description = "ID of the country", example = "1") @PathVariable Long id) {
        CountryDTO country = countryService.getById(id);
        if (country == null) {
            return ResponseWrapper.notFound("Country");
        }
        return ResponseWrapper.found(country, "Country");
    }

    @Operation(summary = "Get a country by its name", description = "Retrieve detailed information about a country by its name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Country found"),
            @ApiResponse(responseCode = "404", description = "Country not found")
    })
    @GetMapping("/name/{name}")
    public ResponseWrapper<CountryDTO> getCountryByName(
            @Parameter(description = "Name of the country", example = "Japan") @Valid @PathVariable String name) {
        CountryDTO country = countryService.getByName(name);
        if (country == null) {
            return ResponseWrapper.notFound("Country");
        }
        return ResponseWrapper.found(country, "Country");
    }

    @Operation(summary = "Get countries by continent", description = "Retrieve a list of countries belonging to a specific continent.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Countries found"),
            @ApiResponse(responseCode = "400", description = "Invalid continent")
    })
    @GetMapping("/by-continent/{continentSTR}")
    public ResponseWrapper<List<CountryDTO>> getCountryByContinent(
            @Parameter(description = "Continent name in uppercase", example = "ASIA") @Valid @PathVariable String continentSTR) {
        Continent continent = Continent.valueOf(continentSTR.toUpperCase());
        List<CountryDTO> countries = countryService.getByContinent(continent);
        return ResponseWrapper.found(countries, "Countries");
    }

    @Operation(summary = "Get all countries", description = "Retrieve a list of all countries.")
    @ApiResponse(responseCode = "200", description = "List of countries retrieved successfully")
    @GetMapping
    public ResponseWrapper<List<CountryDTO>> getAllCountries() {
        List<CountryDTO> countries = countryService.getAll();
        return ResponseWrapper.found(countries, "Countries");
    }

    @Operation(
            summary = "Create a new country",
            description = "Create a new country entry. **Requires ADMIN role**.",
            security = @SecurityRequirement(name = "admin")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Country created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or invalid input")
    })
    @PostMapping
    public ResponseWrapper<CountryDTO> createCountry(@Valid @RequestBody CountryInsertDTO insertDTO) {
        Result<Void> validationResult = countryService.validate(insertDTO);
        if (!validationResult.isSuccess()) {
            return ResponseWrapper.badRequest(validationResult.getErrorMessage());
        }
        CountryDTO createdCountry = countryService.create(insertDTO);
        return ResponseWrapper.created(createdCountry, "Country");
    }

    @Operation(
            summary = "Delete a country by its ID",
            description = "Delete an existing country. **Requires ADMIN role**.",
            security = @SecurityRequirement(name = "admin")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Country deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Country not found")
    })
    @DeleteMapping("/{id}")
    public ResponseWrapper<Void> deleteCountry(
            @Parameter(description = "ID of the country to delete", example = "1") @PathVariable Long id) {
        if (countryService.getById(id) == null) {
            return ResponseWrapper.notFound("Country");
        }
        countryService.delete(id);
        return ResponseWrapper.deleted("Country");
    }
}
