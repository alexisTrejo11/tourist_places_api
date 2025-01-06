package at.backend.tourist.places.Service;

import at.backend.tourist.places.DTOs.TouristPlaceDTO;
import at.backend.tourist.places.DTOs.TouristPlaceInsertDTO;
import at.backend.tourist.places.Models.TouristPlace;

import java.util.List;

public interface TouristPlaceService extends CommonService<TouristPlaceDTO, TouristPlaceInsertDTO> {
    List<TouristPlace> findByCountry(Long countryId);
    List<TouristPlace> findByCategory(Long categoryId);
}