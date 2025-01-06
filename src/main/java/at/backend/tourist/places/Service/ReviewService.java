package at.backend.tourist.places.Service;

import at.backend.tourist.places.DTOs.ReviewDTO;
import at.backend.tourist.places.DTOs.ReviewInsertDTO;
import at.backend.tourist.places.Models.Review;

import java.util.List;

public interface ReviewService extends CommonService<ReviewDTO, ReviewInsertDTO> {
    List<Review> findByTouristPlace(Long touristPlaceId);
}
