package at.backend.tourist.places.modules.Review.Controller;

import at.backend.tourist.places.core.SwaggerHelper.ApiResponseExamples;
import at.backend.tourist.places.core.Utils.Response.ResponseWrapper;
import at.backend.tourist.places.modules.Review.DTOs.ReviewDTO;
import at.backend.tourist.places.modules.Review.DTOs.ReviewInsertDTO;
import at.backend.tourist.places.modules.Review.Service.ReviewService;
import at.backend.tourist.places.modules.Places.Service.TouristPlaceService;
import at.backend.tourist.places.core.Utils.Response.Result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

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

    @Operation(summary = "Get all reviews", description = "Fetches all reviews in the system. **Requires ADMIN role**.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of all reviews retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseWrapper.class), examples = @ExampleObject(value = ApiResponseExamples.REVIEWS))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.UNAUTHORIZED_ACCESS)))
    })
    @GetMapping
    public ResponseWrapper<List<ReviewDTO>> getAllReviews() {
        return ResponseWrapper.found(reviewService.getAll(), "Reviews");
    }

    @Operation(summary = "Get reviews by tourist place ID",description = "Fetches all reviews associated with a specific tourist place. **Requires ADMIN role**.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseWrapper.class), examples = @ExampleObject(value = ApiResponseExamples.REVIEWS))),
            @ApiResponse(responseCode = "404", description = "No reviews found",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NOT_FOUND)))
    })
    @GetMapping("tourist_place/{touristPlaceId}")
    public ResponseEntity<List<ReviewDTO>> getByTouristPlaceId(
            @Parameter(description = "ID of the tourist place", example = "101", required = true)
            @PathVariable Long touristPlaceId) {

        List<ReviewDTO> reviews = reviewService.getByTouristPlace(touristPlaceId);
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Get review by ID", description = "Fetches a specific review by its ID. **Requires ADMIN role**.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Review retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseWrapper.class), examples = @ExampleObject(value = ApiResponseExamples.REVIEW))),
            @ApiResponse(responseCode = "404", description = "Review not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NOT_FOUND)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<ReviewDTO>> getReviewById(
            @Parameter(description = "ID of the review", example = "1", required = true)
            @PathVariable Long id) {

        ReviewDTO review = reviewService.getById(id);
        return ResponseEntity.ok(ResponseWrapper.found(review, "Review"));
    }

    @Operation(summary = "Create a new review", description = "Creates a new review with the provided details. **Requires ADMIN role**.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Review created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseWrapper.class), examples = @ExampleObject(value = ApiResponseExamples.REVIEW_CREATED))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.UNAUTHORIZED_ACCESS)))
    })
    @PostMapping
    public ResponseEntity<ResponseWrapper<ReviewDTO>> createReview(
            @Valid @RequestBody ReviewInsertDTO insertDTO) {

        ReviewDTO createdReview = reviewService.create(insertDTO);
        touristPlaceService.updatePlaceRating(createdReview.getPlaceId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created(createdReview, "Review"));
    }

    @Operation(summary = "Delete a review", description = "Deletes a review by its ID. **Requires ADMIN role**.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Review deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.SUCCESS))),
            @ApiResponse(responseCode = "404", description = "Review not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.NOT_FOUND))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.UNAUTHORIZED_ACCESS)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Void>> deleteReview(
            @Parameter(description = "ID of the review to delete", example = "1", required = true)
            @PathVariable Long id) {

        reviewService.delete(id);
        touristPlaceService.updatePlaceRating(id);
        return ResponseEntity.ok(ResponseWrapper.deleted("Review"));
    }
}
