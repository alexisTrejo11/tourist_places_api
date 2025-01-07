package at.backend.tourist.places.Service;

import at.backend.tourist.places.DTOs.ReviewDTO;
import at.backend.tourist.places.DTOs.ReviewInsertDTO;
import at.backend.tourist.places.Models.TouristPlace;
import at.backend.tourist.places.Utils.Result;

import java.util.List;

public interface ReviewService extends CommonService<ReviewDTO, ReviewInsertDTO> {
    List<ReviewDTO> getByTouristPlace(Long touristPlaceId);
    Result<TouristPlace> validate(ReviewInsertDTO insertDTO);
}
