package at.backend.tourist.places.modules.Places.Service;

import at.backend.tourist.places.modules.Places.DTOs.TouristPlaceDTO;
import at.backend.tourist.places.modules.Places.DTOs.TouristPlaceInsertDTO;
import at.backend.tourist.places.modules.Places.DTOs.TouristPlaceSearchDTO;
import at.backend.tourist.places.core.Service.CommonService;
import at.backend.tourist.places.modules.Places.Models.PlaceRelationships;
import at.backend.tourist.places.core.Utils.Response.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface TouristPlaceService extends CommonService<TouristPlaceDTO, TouristPlaceInsertDTO> {
    Page<TouristPlaceDTO> searchTouristPlaces(TouristPlaceSearchDTO searchDTO, Pageable pageable);
    List<TouristPlaceDTO> getByCountry(Long countryId);
    List<TouristPlaceDTO> getByCategory(Long categoryId);
    List<TouristPlaceDTO> getByIdList(Set<Long> idsList);
    Result<PlaceRelationships> validate(TouristPlaceInsertDTO insertDTO);

    void updatePlaceRating(Long id);
}