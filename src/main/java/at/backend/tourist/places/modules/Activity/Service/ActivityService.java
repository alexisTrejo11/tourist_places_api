package at.backend.tourist.places.modules.Activity.Service;

import at.backend.tourist.places.modules.Activity.DTOs.ActivityDTO;
import at.backend.tourist.places.modules.Activity.DTOs.ActivityInsertDTO;
import at.backend.tourist.places.modules.Places.Models.TouristPlace;
import at.backend.tourist.places.core.Service.CommonService;
import at.backend.tourist.places.core.Utils.Response.Result;

import java.util.List;

public interface ActivityService extends CommonService<ActivityDTO, ActivityInsertDTO> {
    List<ActivityDTO> getByTouristPlace(Long touristPlaceId);
    Result<TouristPlace> validate(ActivityInsertDTO insertDTO);
}

