package at.backend.tourist.places.modules.Places.Controller;

import at.backend.tourist.places.core.Utils.ResponseWrapper;
import at.backend.tourist.places.modules.Places.DTOs.PlaceListDTO;
import at.backend.tourist.places.modules.Places.Service.PlaceListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/place-lists")
@RequiredArgsConstructor
public class PlaceListController {

    private final PlaceListService placeListService;

    @GetMapping("/{id}")
    public ResponseWrapper<PlaceListDTO> getById(@PathVariable Long id) {
        PlaceListDTO placeList = placeListService.getById(id);
        if (placeList == null) {
            return ResponseWrapper.notFound("Place List");
        }
        return ResponseWrapper.found(placeList, "Place List");
    }

    @GetMapping
    public ResponseWrapper<List<PlaceListDTO>> getAll() {
        List<PlaceListDTO> placeLists = placeListService.getAll();
        return ResponseWrapper.found(placeLists, "Place Lists");
    }

    @GetMapping("/user/{userId}")
    public ResponseWrapper<List<PlaceListDTO>> getByUserId(@PathVariable Long userId) {
        List<PlaceListDTO> placeLists = placeListService.getByUserId(userId);
        if (placeLists.isEmpty()) {
            return ResponseWrapper.notFound("Place Lists for User ID: " + userId);
        }
        return ResponseWrapper.found(placeLists, "Place Lists");
    }

    @DeleteMapping("/{id}")
    public ResponseWrapper<Void> delete(@PathVariable Long id) {
        PlaceListDTO placeList = placeListService.getById(id);
        if (placeList == null) {
            return ResponseWrapper.notFound("Place List");
        }
        placeListService.delete(id);
        return ResponseWrapper.deleted("Place List");
    }
}
