package at.backend.tourist.places.modules.Review.Service;

import at.backend.tourist.places.modules.Review.AutoMappers.ReviewMapper;
import at.backend.tourist.places.modules.Review.DTOs.ReviewDTO;
import at.backend.tourist.places.modules.Review.DTOs.ReviewInsertDTO;
import at.backend.tourist.places.modules.Review.DTOs.ReviewUpdateDTO;
import at.backend.tourist.places.modules.Review.Repository.ReviewRepository;
import at.backend.tourist.places.modules.Review.Review;
import at.backend.tourist.places.modules.Places.TouristPlace;
import at.backend.tourist.places.modules.User.Model.User;
import at.backend.tourist.places.modules.Places.Service.TouristPlaceRepository;
import at.backend.tourist.places.modules.User.Repository.UserRepository;
import at.backend.tourist.places.core.Utils.Result;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<Review> optionalReview = reviewRepository.findById(id);
        return optionalReview
                .map(reviewMapper::entityToDTO)
                .orElse(null);

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
        boolean isPlaceExisting = touristPlaceRepository.existsById(touristPlaceId);
        if (!isPlaceExisting) {
            return null;
        }

        List<Review> reviews = reviewRepository.findByPlaceId(touristPlaceId);

        return reviews.stream()
                .map(reviewMapper::entityToDTO)
                .toList();
    }

    @Override
    public Page<ReviewDTO> getReviewByEmail(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Page<Review> reviewsPage = reviewRepository.findByAuthorId(user.getId(), pageable);
        return reviewsPage.map(reviewMapper::entityToDTO);
    }

    @Override
    public Result<Void> validate(ReviewInsertDTO insertDTO) {
        if (insertDTO.getRating() < 0 ||insertDTO.getRating() > 10) {
            return Result.failure("Rating out of range");
        }

        return Result.success();
    }

    @Override
    public Result<Void> validate(ReviewUpdateDTO updateDTO, String userEmail) {
        if (updateDTO.getRating() < 0 ||updateDTO.getRating() > 10) {
            return Result.failure("Rating out of range");
        }

        validateUserBelonging(updateDTO.getReviewId(), userEmail);

        return Result.success();
    }

    @Override
    public ReviewDTO create(ReviewInsertDTO insertDTO) {
        log.info("Starting to create review for tourist place: {}", insertDTO.getPlaceId());

        Review review = reviewMapper.DTOToEntity(insertDTO);
        addRelationship(insertDTO, review);

        reviewRepository.saveAndFlush(review);

        log.info("Review created successfully with ID: {}", review.getId());

        return reviewMapper.entityToDTO(review);
    }

    public ReviewDTO update(ReviewUpdateDTO updateDTO, String email) {
        log.info("Starting to update review for tourist place: {}", updateDTO.getReviewId());
        Review review = reviewRepository.findById(updateDTO.getReviewId())
                .orElseThrow(() -> new EntityNotFoundException("Review not Found"));

        reviewMapper.update(review, updateDTO);

        reviewRepository.saveAndFlush(review);

        log.info("Review updated successfully with ID: {}", review.getId());

        return reviewMapper.entityToDTO(review);
    }

    @Override
    public void delete(Long id) {
        log.info("Attempting to delete review with ID: {}", id);

        boolean exists = reviewRepository.existsById(id);
        if (!exists) {
            log.error("Review with ID: {} not found for deletion", id);
            throw new EntityNotFoundException("Review not found");
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
            throw new RuntimeException("Author not provided");
        }

        return userRepository.findByEmail(authorEmail)
                .orElseThrow(() -> new EntityNotFoundException("Email not found"));
    }

    private TouristPlace getPlace(Long placeId) {
        return touristPlaceRepository.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException("Place not found"));
    }

    private void validateUserBelonging(Long reviewId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Check if user make the request comment
        Optional<Review> review = reviewRepository.findByIdAndAuthorId(reviewId, user.getId());
        if (review.isEmpty()) {
            throw new EntityNotFoundException("Review not found");
        }
    }

}