package at.backend.tourist.places.Controller;

import at.backend.tourist.places.DTOs.ReviewDTO;
import at.backend.tourist.places.DTOs.ReviewInsertDTO;
import at.backend.tourist.places.Models.TouristPlace;
import at.backend.tourist.places.Service.ReviewService;
import at.backend.tourist.places.Service.TouristPlaceService;
import at.backend.tourist.places.Utils.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final TouristPlaceService touristPlaceService;

    @GetMapping
    public List<ReviewDTO> getAllActivities() {
        return reviewService.getAll();
    }

    @GetMapping("tourist_place/{touristPlaceId}")
    public ResponseEntity<List<ReviewDTO>> getByTouristPlaceId(@PathVariable Long touristPlaceId) {
        List<ReviewDTO> review = reviewService.getByTouristPlace(touristPlaceId);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(review);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        ReviewDTO review = reviewService.getById(id);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(review);
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody ReviewInsertDTO insertDTO) {
        Result<TouristPlace> validationResult = reviewService.validate(insertDTO);

        insertDTO.setTouristPlace(validationResult.getData());
        ReviewDTO createdReview = reviewService.create(insertDTO);

        touristPlaceService.updatePlaceRating(createdReview.getPlaceId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        if (reviewService.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

