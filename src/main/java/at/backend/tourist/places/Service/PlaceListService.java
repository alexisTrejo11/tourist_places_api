package at.backend.tourist.places.Service;

import at.backend.tourist.places.DTOs.PlaceCategoryDTO;
import at.backend.tourist.places.DTOs.PlaceCategoryInsertDTO;
import at.backend.tourist.places.DTOs.PlaceListDTO;
import at.backend.tourist.places.DTOs.PlaceListInsertDTO;

import java.util.List;
import java.util.Set;

public interface PlaceListService extends CommonService<PlaceListDTO, PlaceListInsertDTO> {
    PlaceListDTO removePlaces(Long placeListId, Set<Long> placeIds);
    List<PlaceListDTO> getByUserId(Long userId);
    PlaceListDTO addPlaces(Long placeListId, Set<Long> placeIds);
    void delete(Long id, Long userId);

}