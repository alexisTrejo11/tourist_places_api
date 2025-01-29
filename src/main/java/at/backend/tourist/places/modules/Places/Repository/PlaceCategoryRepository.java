package at.backend.tourist.places.modules.Places.Repository;

import at.backend.tourist.places.modules.Places.Models.PlaceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceCategoryRepository extends JpaRepository<PlaceCategory, Long> {
}
