package at.backend.tourist.places.modules.Places.Service;

import at.backend.tourist.places.modules.Places.PlaceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceListRepository extends JpaRepository<PlaceList, Long> {
    List<PlaceList> findByUserId(Long userId);
    Optional<PlaceList> findByIdAndUserId(Long id, Long userId);
}
