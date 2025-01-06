package at.backend.tourist.places.Service;

import at.backend.tourist.places.AutoMappers.ReviewMapper;
import at.backend.tourist.places.DTOs.ReviewDTO;
import at.backend.tourist.places.DTOs.ReviewInsertDTO;
import at.backend.tourist.places.Models.Review;
import at.backend.tourist.places.Repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public List<Review> findByTouristPlace(Long touristPlaceId) {
        return reviewRepository.findByPlaceId(touristPlaceId);
    }

    @Override
    public ReviewDTO create(ReviewInsertDTO insertDTO) {
        Review activity = reviewMapper.DTOToEntity(insertDTO);

        reviewRepository.saveAndFlush(activity);

        return reviewMapper.entityToDTO(activity);
    }

    @Override
    public ReviewDTO getById(Long id) {
        Optional<Review> optionalReview = reviewRepository.findById(id);
        return optionalReview
                .map(reviewMapper::entityToDTO)
                .orElse(null);

    }

    @Override
    public List<ReviewDTO> getAll() {
        List<Review> activities = reviewRepository.findAll();

        return activities.stream()
                .map(reviewMapper::entityToDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        boolean exists = reviewRepository.existsById(id);
        if (!exists) {
            throw new EntityNotFoundException("Review not found");
        }

        reviewRepository.deleteById(id);
    }
}