package at.backend.tourist.places.modules.Activity.Repository;

import at.backend.tourist.places.modules.Activity.Model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByTouristPlaceId(Long touristPlaceId);
}
