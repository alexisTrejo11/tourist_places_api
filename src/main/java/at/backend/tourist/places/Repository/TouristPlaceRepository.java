package at.backend.tourist.places.Repository;

import at.backend.tourist.places.Models.TouristPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TouristPlaceRepository extends JpaRepository<TouristPlace, Long> {
    List<TouristPlace> findByCountryId(Long countryId);

    @Query("SELECT t FROM TouristPlace t LEFT JOIN FETCH t.reviews WHERE t.id = :id")
    Optional<TouristPlace> findByIdWithReviews(Long id);

    List<TouristPlace> findByCategoryId(Long categoryId);
}
