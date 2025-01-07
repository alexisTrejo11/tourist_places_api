package at.backend.tourist.places.Service;

import at.backend.tourist.places.AutoMappers.ReviewMapper;
import at.backend.tourist.places.DTOs.ReviewDTO;
import at.backend.tourist.places.DTOs.ReviewInsertDTO;
import at.backend.tourist.places.Models.Review;
import at.backend.tourist.places.Models.TouristPlace;
import at.backend.tourist.places.Repository.ReviewRepository;
import at.backend.tourist.places.Repository.TouristPlaceRepository;
import at.backend.tourist.places.Utils.Result;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
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
        boolean isPlaceExisiting = touristPlaceRepository.existsById(touristPlaceId);
        if (!isPlaceExisiting) {
            return null;
        }

        List<Review> reviews = reviewRepository.findByPlaceId(touristPlaceId);

        return reviews.stream()
                .map(reviewMapper::entityToDTO)
                .toList();
    }

    @Override
    public Result<TouristPlace> validate(ReviewInsertDTO insertDTO) {
        Optional<TouristPlace> touristPlace = touristPlaceRepository.findById(insertDTO.getPlaceId());
        if (touristPlace.isEmpty()) {
            return Result.failure("Tourist place ID");
        }

        if (insertDTO.getRating() < 0 ||insertDTO.getRating() > 10) {
            return Result.failure("Rating out of range");
        }

        return Result.success(touristPlace.get());
    }

    @Override
    public ReviewDTO create(ReviewInsertDTO insertDTO) {
        log.info("Starting to create review for tourist place: {}", insertDTO.getTouristPlace());

        Review review = reviewMapper.DTOToEntity(insertDTO);
        review.setPlace(insertDTO.getTouristPlace());

        reviewRepository.saveAndFlush(review);

        log.info("Review created successfully with ID: {}", review.getId());

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
}