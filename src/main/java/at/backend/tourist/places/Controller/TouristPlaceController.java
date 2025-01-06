package at.backend.tourist.places.Controller;

import at.backend.tourist.places.DTOs.TouristPlaceDTO;
import at.backend.tourist.places.DTOs.TouristPlaceInsertDTO;
import at.backend.tourist.places.Service.TouristPlaceService;
import at.backend.tourist.places.Utils.PlaceRelationships;
import at.backend.tourist.places.Utils.Result;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/tourist_places")
public class TouristPlaceController {

    @Autowired
    private TouristPlaceService touristPlaceService;

    @GetMapping
    public List<TouristPlaceDTO> getAllCountries() {
        return touristPlaceService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TouristPlaceDTO> getTouristPlaceById(@PathVariable Long id) {
        TouristPlaceDTO country = touristPlaceService.getById(id);
        if (country == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(country);
    }

    @GetMapping("/country/{countryId}")
    public ResponseEntity<List<TouristPlaceDTO>> getByCountryId(@PathVariable Long countryId) {
        List<TouristPlaceDTO> places = touristPlaceService.getByCountry(countryId);
        if (places == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(places);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TouristPlaceDTO>> getByCategoryId(@PathVariable Long categoryId) {
        List<TouristPlaceDTO> places = touristPlaceService.getByCategory(categoryId);
        if (places == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(places);
    }


    @PostMapping
    public ResponseEntity<?> createTouristPlace(@Valid @RequestBody TouristPlaceInsertDTO insertDTO) {
        Result<PlaceRelationships> validationResult = touristPlaceService.validate(insertDTO);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult.getErrorMessage());
        }

        insertDTO.setPlaceRelationships(validationResult.getData());

        TouristPlaceDTO createdTouristPlace = touristPlaceService.create(insertDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTouristPlace);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTouristPlace(@PathVariable Long id) {
        if (touristPlaceService.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        touristPlaceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

