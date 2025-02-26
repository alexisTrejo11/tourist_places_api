package at.backend.tourist.places.Controller;

import at.backend.tourist.places.core.Utils.Response.ResponseWrapper;
import at.backend.tourist.places.modules.Auth.JWT.JwtService;
import at.backend.tourist.places.modules.Places.Service.TouristPlaceService;
import at.backend.tourist.places.modules.Review.DTOs.ReviewDTO;
import at.backend.tourist.places.modules.Review.DTOs.ReviewInsertDTO;
import at.backend.tourist.places.modules.Review.DTOs.ReviewUpdateDTO;
import at.backend.tourist.places.modules.Review.Service.ReviewService;
import at.backend.tourist.places.modules.User.Controller.UserReviewController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @Mock
    private JwtService jwtService;

    @Mock
    private TouristPlaceService touristPlaceService;

    @InjectMocks
    private UserReviewController userReviewController;

    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
    }

    @Test
    void testGetMyReviews() {
        String email = "user@example.com";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("updatedAt").descending());
        Page<ReviewDTO> page = new PageImpl<>(List.of(new ReviewDTO()));

        when(jwtService.getEmailFromRequest(request)).thenReturn(email);
        when(reviewService.getReviewByEmail(email, pageable)).thenReturn(page);

        ResponseWrapper<Page<ReviewDTO>> response = userReviewController.getMyReviews(request, 0, 10, "updatedAt", "DESC");

        assertNotNull(response);
        assertEquals(1, response.getData().getContent().size());
    }

    @Test
    void testNewReview() {
        String email = "user@example.com";
        ReviewInsertDTO insertDTO = new ReviewInsertDTO();
        insertDTO.setAuthorEmail(email);
        ReviewDTO reviewDTO = new ReviewDTO();

        when(jwtService.getEmailFromRequest(request)).thenReturn(email);
        when(reviewService.create(insertDTO)).thenReturn(reviewDTO);

        ResponseEntity<ResponseWrapper<ReviewDTO>> response = userReviewController.newReview(insertDTO, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateMyReview() {
        String email = "user@example.com";
        ReviewUpdateDTO updateDTO = new ReviewUpdateDTO();
        ReviewDTO updatedReview = new ReviewDTO();

        when(jwtService.getEmailFromRequest(request)).thenReturn(email);
        when(reviewService.update(updateDTO, email)).thenReturn(updatedReview);

        ResponseEntity<ResponseWrapper<ReviewDTO>> response = userReviewController.updateMyReview(updateDTO, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testDeleteMyReview() {
        String email = "user@example.com";
        Long reviewId = 1L;

        when(jwtService.getEmailFromRequest(request)).thenReturn(email);
        doNothing().when(reviewService).delete(reviewId, email);

        ResponseEntity<ResponseWrapper<Void>> response = userReviewController.deleteMyReview(reviewId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
