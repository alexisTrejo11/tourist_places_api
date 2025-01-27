package at.backend.tourist.places.modules.Places.Service;

import at.backend.tourist.places.modules.Places.DTOs.PlaceListDTO;
import at.backend.tourist.places.modules.Places.DTOs.PlaceListInsertDTO;
import at.backend.tourist.places.core.Service.CommonService;

import java.util.List;
import java.util.Set;

public interface PlaceListService extends CommonService<PlaceListDTO, PlaceListInsertDTO> {
    PlaceListDTO removePlaces(Long placeListId, Set<Long> placeIds);
    List<PlaceListDTO> getByUserId(Long userId);
    PlaceListDTO addPlaces(Long placeListId, Set<Long> placeIds);
    void delete(Long id, Long userId);

}