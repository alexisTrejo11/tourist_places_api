package at.backend.tourist.places.Controller;


import at.backend.tourist.places.core.Utils.Response.ResponseWrapper;
import at.backend.tourist.places.modules.Review.Controller.ReviewController;
import at.backend.tourist.places.modules.Review.DTOs.ReviewDTO;
import at.backend.tourist.places.modules.Review.DTOs.ReviewInsertDTO;
import at.backend.tourist.places.modules.Review.Service.ReviewService;
import at.backend.tourist.places.modules.Places.Service.TouristPlaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @Mock
    private TouristPlaceService touristPlaceService;

    @InjectMocks
    private ReviewController reviewController;

    private ReviewDTO reviewDTO;
    private ReviewInsertDTO reviewInsertDTO;

    @BeforeEach
    void setUp() {
        reviewDTO = new ReviewDTO();
        reviewDTO.setId(1L);
        reviewDTO.setAuthorId("2");
        reviewDTO.setComment("Great place!");
        reviewDTO.setRating(5);

        reviewInsertDTO = new ReviewInsertDTO();
        reviewInsertDTO.setAuthorEmail("email@test.com");
        reviewDTO.setComment("Great place!");
        reviewDTO.setRating(5);
    }

    @Test
    void getAllReviews_ShouldReturnReviewList() {
        when(reviewService.getAll()).thenReturn(Collections.singletonList(reviewDTO));

        ResponseWrapper<List<ReviewDTO>> response = reviewController.getAllReviews();

        assertNotNull(response);
        assertFalse(response.getData().isEmpty());
        assertEquals(1, response.getData().size());
        assertEquals("Reviews data successfully Fetched", response.getMessage() );
    }

    @Test
    void getByTouristPlaceId_ShouldReturnReviews() {
        when(reviewService.getByTouristPlace(anyLong())).thenReturn(Collections.singletonList(reviewDTO));

        ResponseEntity<List<ReviewDTO>> response = reviewController.getByTouristPlaceId(101L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getReviewById_ShouldReturnReview() {
        when(reviewService.getById(anyLong())).thenReturn(reviewDTO);

        ResponseEntity<ResponseWrapper<ReviewDTO>> response = reviewController.getReviewById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getData().getId());
    }

    @Test
    void createReview_ShouldReturnCreatedReview() {
        when(reviewService.create(any(ReviewInsertDTO.class))).thenReturn(reviewDTO);
        Mockito.doNothing().when(touristPlaceService).updatePlaceRating(anyLong());

        ResponseEntity<ResponseWrapper<ReviewDTO>> response = reviewController.createReview(reviewInsertDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Review", response.getBody().getMessage());
    }

    @Test
    void deleteReview_ShouldReturnSuccessResponse() {
        Mockito.doNothing().when(reviewService).delete(anyLong());
        Mockito.doNothing().when(touristPlaceService).updatePlaceRating(anyLong());

        ResponseEntity<ResponseWrapper<Void>> response = reviewController.deleteReview(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Review", response.getBody().getMessage());
    }
}

