package at.backend.tourist.places.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import at.backend.tourist.places.core.Exceptions.BusinessLogicException;
import at.backend.tourist.places.core.Exceptions.ResourceNotFoundException;
import at.backend.tourist.places.modules.Review.AutoMappers.ReviewMapper;
import at.backend.tourist.places.modules.Review.DTOs.ReviewDTO;
import at.backend.tourist.places.modules.Review.DTOs.ReviewInsertDTO;
import at.backend.tourist.places.modules.Review.Repository.ReviewRepository;
import at.backend.tourist.places.modules.Review.Review;
import at.backend.tourist.places.modules.Places.Models.TouristPlace;
import at.backend.tourist.places.modules.User.Model.User;
import at.backend.tourist.places.modules.Places.Repository.TouristPlaceRepository;
import at.backend.tourist.places.modules.User.Repository.UserRepository;
import at.backend.tourist.places.modules.Review.Service.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TouristPlaceRepository touristPlaceRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Review review;
    private ReviewDTO reviewDTO;
    private ReviewInsertDTO reviewInsertDTO;
    private User user;
    private TouristPlace place;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        place = new TouristPlace();
        place.setId(1L);

        review = new Review();
        review.setId(1L);
        review.setAuthor(user);
        review.setPlace(place);
        review.setRating(5d);

        reviewDTO = new ReviewDTO();
        reviewDTO.setId(1L);

        reviewInsertDTO = new ReviewInsertDTO();
        reviewInsertDTO.setAuthorEmail("user@example.com");
        reviewInsertDTO.setPlaceId(1L);
        reviewInsertDTO.setRating(5);
    }

    @Test
    void getById_ShouldReturnReviewDTO_WhenReviewExists() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewMapper.entityToDTO(review)).thenReturn(reviewDTO);

        ReviewDTO result = reviewService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getById_ShouldThrowException_WhenReviewNotFound() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.getById(1L));
    }

    @Test
    void create_ShouldReturnReviewDTO_WhenValidInput() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(touristPlaceRepository.findById(1L)).thenReturn(Optional.of(place));
        when(reviewMapper.DTOToEntity(reviewInsertDTO)).thenReturn(review);
        when(reviewRepository.saveAndFlush(review)).thenReturn(review);
        when(reviewMapper.entityToDTO(review)).thenReturn(reviewDTO);

        ReviewDTO result = reviewService.create(reviewInsertDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void create_ShouldThrowException_WhenInvalidRating() {
        reviewInsertDTO.setRating(11);
        assertThrows(BusinessLogicException.class, () -> reviewService.create(reviewInsertDTO));
    }

    @Test
    void delete_ShouldThrowException_WhenReviewNotFound() {
        when(reviewRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> reviewService.delete(1L));
    }
}

