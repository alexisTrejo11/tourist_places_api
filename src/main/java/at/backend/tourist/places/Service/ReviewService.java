package at.backend.tourist.places.Service;

import at.backend.tourist.places.DTOs.ReviewDTO;
import at.backend.tourist.places.DTOs.ReviewInsertDTO;
import at.backend.tourist.places.DTOs.ReviewUpdateDTO;
import at.backend.tourist.places.Utils.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService extends CommonService<ReviewDTO, ReviewInsertDTO> {
    List<ReviewDTO> getByTouristPlace(Long touristPlaceId);
    Page<ReviewDTO> getReviewByEmail(String email, Pageable pageable);

    Result<Void> validate(ReviewInsertDTO insertDTO);
    Result<Void> validate(ReviewUpdateDTO updateDTO, String userEmail);

    ReviewDTO update(ReviewUpdateDTO updateDTO, String email);
    void delete(Long id, String email);

}
