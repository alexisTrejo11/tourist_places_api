package at.backend.tourist.places.Controller;

import at.backend.tourist.places.DTOs.PlaceListDTO;
import at.backend.tourist.places.DTOs.PlaceListInsertDTO;
import at.backend.tourist.places.JWT.JwtService;
import at.backend.tourist.places.Service.PlaceListService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/users/lists")
public class UserPlaceListController {

    private final PlaceListService placeListService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<PlaceListDTO> newList(@Valid @RequestBody PlaceListInsertDTO insertDTO) {
        PlaceListDTO createdList = placeListService.create(insertDTO);
        return ResponseEntity.ok(createdList);
    }

    @GetMapping("/my")
    public ResponseEntity<List<PlaceListDTO>> getMyLists(HttpServletRequest request) {
        Long userId = jwtService.getIdFromRequest(request);

        List<PlaceListDTO> placeLists = placeListService.getByUserId(userId);
        return ResponseEntity.ok(placeLists);
    }

    @PostMapping("/{placeListId}/add-place")
    public ResponseEntity<PlaceListDTO> addPlaces(@PathVariable Long placeListId, @RequestBody Set<Long> placeIds) {
        PlaceListDTO updatedList = placeListService.addPlaces(placeListId, placeIds);
        return ResponseEntity.ok(updatedList);
    }

    @PostMapping("/{placeListId}/remove-place")
    public ResponseEntity<PlaceListDTO> removePlaces(@PathVariable Long placeListId, @RequestBody Set<Long> placeIds) {
        PlaceListDTO updatedList = placeListService.removePlaces(placeListId, placeIds);
        return ResponseEntity.ok(updatedList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyList(@PathVariable Long id,
                                             HttpServletRequest request) {
        Long userId = jwtService.getIdFromRequest(request);

        placeListService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

}
