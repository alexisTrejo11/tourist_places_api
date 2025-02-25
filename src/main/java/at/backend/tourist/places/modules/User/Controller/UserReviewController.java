package at.backend.tourist.places.modules.User.Controller;

import at.backend.tourist.places.core.SwaggerHelper.ApiResponseExamples;
import at.backend.tourist.places.core.Utils.Response.ResponseWrapper;
import at.backend.tourist.places.modules.Review.DTOs.ReviewDTO;
import at.backend.tourist.places.modules.Review.DTOs.ReviewInsertDTO;
import at.backend.tourist.places.modules.Review.DTOs.ReviewUpdateDTO;
import at.backend.tourist.places.modules.Review.Service.ReviewService;
import at.backend.tourist.places.modules.Places.Service.TouristPlaceService;
import at.backend.tourist.places.modules.Auth.JWT.JwtService;
import at.backend.tourist.places.core.Utils.Response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
    private final JwtService jwtService;
    private final TouristPlaceService touristPlaceService;

    @Operation(summary = "Get user reviews", description = "Fetches user reviews with pagination support.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of reviews retrieved successfully",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = ApiResponseExamples.REVIEW))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = ApiResponseExamples.BAD_REQUEST))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized request",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = ApiResponseExamples.UNAUTHORIZED_ACCESS))
            )
    })
    @GetMapping
    public ResponseWrapper<Page<ReviewDTO>> getMyReviews(
            HttpServletRequest request,
            @Parameter(description = "Page number (zero-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field", example = "createdDate")
            @RequestParam(defaultValue = "updatedAt") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        String email = jwtService.getEmailFromRequest(request);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(sortDirection), sortBy)
        );

        return ResponseWrapper.found(reviewService.getReviewByEmail(email, pageable), "Reviews");
    }

    @Operation(summary = "Create a new review", description = "Allows a user to create a new review for a tourist place.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review created successfully",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = ApiResponseExamples.REVIEW_CREATED))
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = ApiResponseExamples.BAD_REQUEST))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized request",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = ApiResponseExamples.UNAUTHORIZED_ACCESS))
            )
    })
    @PostMapping
    public ResponseEntity<ResponseWrapper<ReviewDTO>> newReview(@Valid @RequestBody ReviewInsertDTO insertDTO,
                                                                HttpServletRequest request) {
        String email = jwtService.getEmailFromRequest(request);
        insertDTO.setAuthorEmail(email);

        ReviewDTO createdReview = reviewService.create(insertDTO);

        touristPlaceService.updatePlaceRating(createdReview.getPlaceId());

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created(createdReview, "Review"));
    }

    @Operation(summary = "Update an existing review", description = "Allows a user to update one of their reviews.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review updated successfully",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = ApiResponseExamples.REVIEW))
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = ApiResponseExamples.BAD_REQUEST))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized request",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = ApiResponseExamples.UNAUTHORIZED_ACCESS))
            ),
            @ApiResponse(responseCode = "404", description = "Review not found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = ApiResponseExamples.NOT_FOUND))
            ),
            @ApiResponse(responseCode = "403", description = "Forbidden request, user does not have permission to update this review",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = ApiResponseExamples.FORBIDDEN))
            )
    })
    @PutMapping
    public ResponseEntity<ResponseWrapper<ReviewDTO>> updateMyReview(@Valid @RequestBody ReviewUpdateDTO updateDTO,
                                                                     HttpServletRequest request) {
        String email = jwtService.getEmailFromRequest(request);

        ReviewDTO createdReview = reviewService.update(updateDTO, email);

        touristPlaceService.updatePlaceRating(createdReview.getPlaceId());

        return ResponseEntity.ok(ResponseWrapper.ok(createdReview, "Review", "update"));
    }

    @Operation(summary = "Delete a review", description = "Deletes a user review by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review deleted successfully",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = ApiResponseExamples.SUCCESS))
            ),
            @ApiResponse(responseCode = "404", description = "Review not found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = ApiResponseExamples.NOT_FOUND))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized request",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = ApiResponseExamples.UNAUTHORIZED_ACCESS))
            ),
            @ApiResponse(responseCode = "403", description = "Forbidden request, user does not have permission to delete this review",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = ApiResponseExamples.FORBIDDEN))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Void>> deleteMyReview(
            @Parameter(description = "ID of the review to be deleted", example = "1")
            @PathVariable Long id,
            HttpServletRequest request) {
        String email = jwtService.getEmailFromRequest(request);

        reviewService.delete(id, email);

        touristPlaceService.updatePlaceRating(id);

        return ResponseEntity.ok(ResponseWrapper.ok("Review", "Delete"));
    }
}