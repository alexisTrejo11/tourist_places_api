package at.backend.tourist.places.Controller;

import at.backend.tourist.places.DTOs.PlaceListDTO;
import at.backend.tourist.places.DTOs.PlaceListInsertDTO;
import at.backend.tourist.places.Service.PlaceListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/place-lists")
@RequiredArgsConstructor
public class PlaceListController {

    private final PlaceListService placeListService;

    @GetMapping("/{id}")
    public ResponseEntity<PlaceListDTO> getById(@PathVariable Long id) {
        PlaceListDTO placeList = placeListService.getById(id);
        if (placeList == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(placeList);
    }

    @GetMapping
    public ResponseEntity<List<PlaceListDTO>> getAll() {
        List<PlaceListDTO> placeLists = placeListService.getAll();
        return ResponseEntity.ok(placeLists);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlaceListDTO>> getByUserId(@PathVariable Long userId) {
        List<PlaceListDTO> placeLists = placeListService.getByUserId(userId);
        return ResponseEntity.ok(placeLists);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        placeListService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

