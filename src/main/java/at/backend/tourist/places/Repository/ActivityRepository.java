package at.backend.tourist.places.Repository;

import at.backend.tourist.places.Models.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByTouristPlaceId(Long touristPlaceId);
}
