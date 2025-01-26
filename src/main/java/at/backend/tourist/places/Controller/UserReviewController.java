package at.backend.tourist.places.Controller;

import at.backend.tourist.places.DTOs.ReviewDTO;
import at.backend.tourist.places.DTOs.ReviewInsertDTO;
import at.backend.tourist.places.DTOs.ReviewUpdateDTO;
import at.backend.tourist.places.Service.ReviewService;
import at.backend.tourist.places.Service.TouristPlaceService;
import at.backend.tourist.places.Utils.JWT.JwtUtil;
import at.backend.tourist.places.Utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/user-reviews")
@RequiredArgsConstructor
@Tag(name = "User Review Management", description = "Endpoints for managing user reviews")
@SecurityRequirement(name = "bearerAuth")
public class UserReviewController {

    private final ReviewService reviewService;
    private final JwtUtil jwtUtil;
    private final TouristPlaceService touristPlaceService;

    @Operation(summary = "Get user reviews", description = "Fetches user reviews with pagination support.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of reviews retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request")
    })
    @GetMapping
    public Page<ReviewDTO> getMyReviews(
            HttpServletRequest request,
            @Parameter(description = "Page number (zero-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field", example = "createdDate")
            @RequestParam(defaultValue = "updatedAt") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        String email = jwtUtil.getEmailFromRequest(request);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(sortDirection), sortBy)
        );

        return reviewService.getReviewByEmail(email, pageable);
    }

    @Operation(summary = "Create a new review", description = "Allows a user to create a new review for a tourist place.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request")
    })
    @PostMapping
    public ResponseEntity<?> newReview(
            @Valid @RequestBody ReviewInsertDTO insertDTO,
            HttpServletRequest request) {

        String email = jwtUtil.getEmailFromRequest(request);
        insertDTO.setAuthorEmail(email);

        Result<Void> validationResult = reviewService.validate(insertDTO);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult.getErrorMessage());
        }

        ReviewDTO createdReview = reviewService.create(insertDTO);
        touristPlaceService.updatePlaceRating(createdReview.getPlaceId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @Operation(summary = "Update an existing review", description = "Allows a user to update one of their reviews.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @PutMapping
    public ResponseEntity<?> updateMyReview(
            @Valid @RequestBody ReviewUpdateDTO updateDTO,
            HttpServletRequest request) {

        String email = jwtUtil.getEmailFromRequest(request);

        Result<Void> validationResult = reviewService.validate(updateDTO, email);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult.getErrorMessage());
        }

        ReviewDTO createdReview = reviewService.update(updateDTO, email);
        touristPlaceService.updatePlaceRating(createdReview.getPlaceId());

        return ResponseEntity.status(HttpStatus.OK).body(createdReview);
    }

    @Operation(summary = "Delete a review", description = "Deletes a user review by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyReview(
            @Parameter(description = "ID of the review to be deleted", example = "1")
            @PathVariable Long id,
            HttpServletRequest request) {
        String email = jwtUtil.getEmailFromRequest(request);

        reviewService.delete(id, email);
        touristPlaceService.updatePlaceRating(id);

        return ResponseEntity.noContent().build();
    }
}
