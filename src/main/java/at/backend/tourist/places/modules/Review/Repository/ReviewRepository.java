package at.backend.tourist.places.modules.Review.Repository;

import at.backend.tourist.places.modules.Review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByPlaceId(Long touristPlaceId);
    Page<Review> findByAuthorId(Long authorID, Pageable pageable);
    Optional<Review> findByIdAndAuthorId(Long id, Long authorId);
}
