package at.backend.tourist.places.Service;

import at.backend.tourist.places.DTOs.TouristPlaceDTO;
import at.backend.tourist.places.DTOs.TouristPlaceInsertDTO;
import at.backend.tourist.places.Models.TouristPlace;
import at.backend.tourist.places.Utils.PlaceRelationships;
import at.backend.tourist.places.Utils.Result;

import java.util.List;

public interface TouristPlaceService extends CommonService<TouristPlaceDTO, TouristPlaceInsertDTO> {
    List<TouristPlace> findByCountry(Long countryId);
    List<TouristPlace> findByCategory(Long categoryId);
    Result<PlaceRelationships> validate(TouristPlaceInsertDTO insertDTO);
}