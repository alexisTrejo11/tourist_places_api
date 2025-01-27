package at.backend.tourist.places.modules.Review.Controller;

import at.backend.tourist.places.modules.Review.DTOs.ReviewDTO;
import at.backend.tourist.places.modules.Review.DTOs.ReviewInsertDTO;
import at.backend.tourist.places.modules.Review.Service.ReviewService;
import at.backend.tourist.places.modules.Places.Service.TouristPlaceService;
import at.backend.tourist.places.core.Utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Review Management", description = "Endpoints for managing reviews as Admin")
@SecurityRequirement(name = "bearerAuth")
public class ReviewController {

    private final ReviewService reviewService;
    private final TouristPlaceService touristPlaceService;

    @Operation(summary = "Get all reviews", description = "Fetches all reviews in the system")
    @ApiResponse(responseCode = "200", description = "List of all reviews retrieved successfully")
    @GetMapping
    public List<ReviewDTO> getAllReviews() {
        return reviewService.getAll();
    }

    @Operation(summary = "Get reviews by tourist place ID",
            description = "Fetches all reviews associated with a specific tourist place")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No reviews found for the given tourist place ID")
    })
    @Parameter(name = "touristPlaceId", description = "ID of the tourist place to filter reviews by", example = "1", required = true)
    @GetMapping("tourist_place/{touristPlaceId}")
    public ResponseEntity<List<ReviewDTO>> getByTouristPlaceId(@PathVariable Long touristPlaceId) {
        List<ReviewDTO> reviews = reviewService.getByTouristPlace(touristPlaceId);
        if (reviews == null || reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Get review by ID", description = "Fetches a specific review by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found with the given ID")
    })
    @Parameter(name = "id", description = "ID of the review to retrieve", example = "1", required = true)
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        ReviewDTO review = reviewService.getById(id);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(review);
    }

    @Operation(summary = "Create a new review", description = "Creates a new review with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid review data provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated")
    })
    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody ReviewInsertDTO insertDTO) {
        Result<Void> validationResult = reviewService.validate(insertDTO);

        ReviewDTO createdReview = reviewService.create(insertDTO);

        touristPlaceService.updatePlaceRating(createdReview.getPlaceId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @Operation(summary = "Delete a review", description = "Deletes a review by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found with the given ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated")
    })
    @Parameter(name = "id", description = "ID of the review to delete", example = "1", required = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        if (reviewService.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        reviewService.delete(id);
        touristPlaceService.updatePlaceRating(id);

        return ResponseEntity.noContent().build();
    }
}
