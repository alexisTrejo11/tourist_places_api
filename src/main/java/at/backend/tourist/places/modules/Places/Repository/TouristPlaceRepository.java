package at.backend.tourist.places.modules.Places.Repository;

import at.backend.tourist.places.modules.Places.Models.TouristPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TouristPlaceRepository extends JpaRepository<TouristPlace, Long>, JpaSpecificationExecutor<TouristPlace>  {
    List<TouristPlace> findByCountryId(Long countryId);
    Set<TouristPlace> findByIdIn(Set<Long> idList);


    @Query("SELECT t FROM TouristPlace t LEFT JOIN FETCH t.reviews WHERE t.id = :id")
    Optional<TouristPlace> findByIdWithReviews(Long id);

    List<TouristPlace> findByCategoryId(Long categoryId);
}
