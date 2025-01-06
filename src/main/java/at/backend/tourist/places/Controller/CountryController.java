package at.backend.tourist.places.Controller;

import at.backend.tourist.places.DTOs.CountryDTO;
import at.backend.tourist.places.DTOs.CountryInsertDTO;
import at.backend.tourist.places.Models.Continent;
import at.backend.tourist.places.Service.CountryService;
import at.backend.tourist.places.Utils.Result;
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

    @GetMapping("/{id}")
    public ResponseEntity<CountryDTO> getCountryById(@PathVariable Long id) {
        CountryDTO country = countryService.getById(id);
        if (country == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(country);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CountryDTO> getCountryByName(@Valid @PathVariable String name) {
        CountryDTO country = countryService.getByName(name);
        if (country == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(country);
    }

    @GetMapping("/by-continent/{continentSTR}")
    public ResponseEntity<List<CountryDTO>> getCountryByContinent(@Valid @PathVariable String continentSTR) {
        Continent continent = Continent.valueOf(continentSTR.toUpperCase());

        List<CountryDTO> countries = countryService.getByContinent(continent);

        return ResponseEntity.ok(countries);
    }

    @GetMapping
    public List<CountryDTO> getAllCountries() {
        return countryService.getAll();
    }


    @PostMapping
    public ResponseEntity<?> createCountry(@Valid @RequestBody CountryInsertDTO insertDTO) {
        Result<Void> validationResult = countryService.validate(insertDTO);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult.getErrorMessage());
        }

        CountryDTO createdCountry = countryService.create(insertDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdCountry);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        if (countryService.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        countryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

