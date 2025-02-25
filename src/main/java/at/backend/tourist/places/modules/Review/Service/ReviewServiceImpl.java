package at.backend.tourist.places.modules.Review.Service;

import at.backend.tourist.places.core.Exceptions.BusinessLogicException;
import at.backend.tourist.places.core.Exceptions.ResourceNotFoundException;
import at.backend.tourist.places.modules.Review.AutoMappers.ReviewMapper;
import at.backend.tourist.places.modules.Review.DTOs.ReviewDTO;
import at.backend.tourist.places.modules.Review.DTOs.ReviewInsertDTO;
import at.backend.tourist.places.modules.Review.DTOs.ReviewUpdateDTO;
import at.backend.tourist.places.modules.Review.Repository.ReviewRepository;
import at.backend.tourist.places.modules.Review.Review;
import at.backend.tourist.places.modules.Places.Models.TouristPlace;
import at.backend.tourist.places.modules.User.Model.User;
import at.backend.tourist.places.modules.Places.Repository.TouristPlaceRepository;
import at.backend.tourist.places.modules.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final TouristPlaceRepository touristPlaceRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewDTO getById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", id));

        return reviewMapper.entityToDTO(review);
    }

    @Override
    public List<ReviewDTO> getAll() {
        List<Review> reviews = reviewRepository.findAll();

        return reviews.stream()
                .map(reviewMapper::entityToDTO)
                .toList();
    }

    @Override
    public List<ReviewDTO> getByTouristPlace(Long touristPlaceId) {
        if (!touristPlaceRepository.existsById(touristPlaceId)) {
            throw new ResourceNotFoundException("Tourist Place", "id", touristPlaceId);
        }

        List<Review> reviews = reviewRepository.findByPlaceId(touristPlaceId);

        return reviews.stream()
                .map(reviewMapper::entityToDTO)
                .toList();
    }

    @Override
    public Page<ReviewDTO> getReviewByEmail(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Page<Review> reviewsPage = reviewRepository.findByAuthorId(user.getId(), pageable);
        return reviewsPage.map(reviewMapper::entityToDTO);
    }

    private void validate(ReviewInsertDTO insertDTO) {
        if (insertDTO.getRating() < 0 || insertDTO.getRating() > 10) {
            throw new BusinessLogicException("Rating must be between 0 and 10");
        }
    }

    private void validate(ReviewUpdateDTO updateDTO, String userEmail) {
        if (updateDTO.getRating() < 0 || updateDTO.getRating() > 10) {
            throw new BusinessLogicException("Rating must be between 0 and 10");
        }

        validateUserBelonging(updateDTO.getReviewId(), userEmail);
    }

    @Override
    public ReviewDTO create(ReviewInsertDTO insertDTO) {
        log.info("Starting to create review for tourist place: {}", insertDTO.getPlaceId());

        validate(insertDTO);

        Review review = reviewMapper.DTOToEntity(insertDTO);
        addRelationship(insertDTO, review);

        reviewRepository.saveAndFlush(review);

        log.info("Review created successfully with ID: {}", review.getId());

        return reviewMapper.entityToDTO(review);
    }

    @Override
    public ReviewDTO update(ReviewUpdateDTO updateDTO, String email) {
        log.info("Starting to update review for tourist place: {}", updateDTO.getReviewId());

        validate(updateDTO, email);

        Review review = reviewRepository.findById(updateDTO.getReviewId())
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", updateDTO.getReviewId()));

        reviewMapper.update(review, updateDTO);

        reviewRepository.saveAndFlush(review);

        log.info("Review updated successfully with ID: {}", review.getId());

        return reviewMapper.entityToDTO(review);
    }

    @Override
    public void delete(Long id) {
        log.info("Attempting to delete review with ID: {}", id);

        if (!reviewRepository.existsById(id)) {
            log.error("Review with ID: {} not found for deletion", id);
            throw new ResourceNotFoundException("Review", "id", id);
        }

        reviewRepository.deleteById(id);
        log.info("Review with ID: {} deleted successfully", id);
    }

    @Override
    public void delete(Long id, String email) {
        log.info("Attempting to delete review with ID: {} and email {}", id, email);

        validateUserBelonging(id, email);

        reviewRepository.deleteById(id);
        log.info("Review with ID: {}  and email {} deleted successfully", id, email);
    }

    private void addRelationship(ReviewInsertDTO insertDTO, Review review) {
        review.setAuthor(getAuthor(insertDTO.getAuthorEmail()));
        review.setPlace(getPlace(insertDTO.getPlaceId()));
    }

    private User getAuthor(String authorEmail) {
        if (authorEmail == null) {
            throw new BusinessLogicException("Author email must be provided");
        }

        return userRepository.findByEmail(authorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", authorEmail));
    }

    private TouristPlace getPlace(Long placeId) {
        return touristPlaceRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Tourist Place", "id", placeId));
    }

    private void validateUserBelonging(Long reviewId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        boolean isUserReview = reviewRepository.findByIdAndAuthorId(reviewId, user.getId()).isPresent();
        if (!isUserReview) {
            throw new BusinessLogicException("User is not authorized to modify this review");
        }
    }
}