package at.backend.tourist.places.Controller;

import at.backend.tourist.places.DTOs.TouristPlaceDTO;
import at.backend.tourist.places.DTOs.TouristPlaceInsertDTO;
import at.backend.tourist.places.Service.TouristPlaceService;
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

    @PostMapping
    public ResponseEntity<TouristPlaceDTO> createTouristPlace(@RequestBody TouristPlaceInsertDTO insertDTO) {
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

