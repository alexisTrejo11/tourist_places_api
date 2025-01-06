package at.backend.tourist.places.Repository;

import at.backend.tourist.places.Models.TouristPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TouristPlaceRepository extends JpaRepository<TouristPlace, Long> {
    List<TouristPlace> findByCountryId(Long countryId);

    List<TouristPlace> findByCategoryId(Long categoryId);
}
