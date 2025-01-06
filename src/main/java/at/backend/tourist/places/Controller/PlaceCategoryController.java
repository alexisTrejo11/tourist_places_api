package at.backend.tourist.places.Controller;

import at.backend.tourist.places.DTOs.PlaceCategoryDTO;
import at.backend.tourist.places.DTOs.PlaceCategoryInsertDTO;
import at.backend.tourist.places.Service.PlaceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/place_categories")
public class PlaceCategoryController {

    @Autowired
    private PlaceCategoryService placeCategoryService;

    @GetMapping
    public List<PlaceCategoryDTO> getAllCountries() {
        return placeCategoryService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceCategoryDTO> getPlaceCategoryById(@PathVariable Long id) {
        PlaceCategoryDTO country = placeCategoryService.getById(id);
        if (country == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(country);
    }

    @PostMapping
    public ResponseEntity<PlaceCategoryDTO> createPlaceCategory(@RequestBody PlaceCategoryInsertDTO insertDTO) {
        PlaceCategoryDTO createdPlaceCategory = placeCategoryService.create(insertDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlaceCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaceCategory(@PathVariable Long id) {
        if (placeCategoryService.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        placeCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

