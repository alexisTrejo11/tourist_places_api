package at.backend.tourist.places.Service;

import at.backend.tourist.places.DTOs.ActivityDTO;
import at.backend.tourist.places.DTOs.ActivityInsertDTO;
import at.backend.tourist.places.Models.TouristPlace;
import at.backend.tourist.places.Utils.Result;

import java.util.List;

public interface ActivityService extends CommonService<ActivityDTO, ActivityInsertDTO> {
    List<ActivityDTO> getByTouristPlace(Long touristPlaceId);
    Result<TouristPlace> validate(ActivityInsertDTO insertDTO);
}

