package at.backend.tourist.places.Service;

import at.backend.tourist.places.DTOs.ActivityDTO;
import at.backend.tourist.places.DTOs.ActivityInsertDTO;

import java.util.List;

public interface ActivityService extends CommonService<ActivityDTO, ActivityInsertDTO> {
    List<ActivityDTO> findByTouristPlace(Long touristPlaceId);
}

