package at.backend.tourist.places.Service;

import at.backend.tourist.places.AutoMappers.ActivityMapper;
import at.backend.tourist.places.DTOs.ActivityDTO;
import at.backend.tourist.places.DTOs.ActivityInsertDTO;
import at.backend.tourist.places.Models.Activity;
import at.backend.tourist.places.Models.TouristPlace;
import at.backend.tourist.places.Repository.ActivityRepository;
import at.backend.tourist.places.Repository.TouristPlaceRepository;
import at.backend.tourist.places.Utils.Result;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final TouristPlaceRepository touristPlaceRepository;
    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    @Override
    public List<ActivityDTO> getByTouristPlace(Long id) {
        boolean isPlaceExisting = touristPlaceRepository.existsById(id);
        if (!isPlaceExisting) {
            return null;
        }

        List<Activity> activities =  activityRepository.findByTouristPlaceId(id);

        return activities.stream()
                .map(activityMapper::entityToDTO)
                .toList();
    }

    @Override
    public Result<TouristPlace> validate(ActivityInsertDTO insertDTO) {
        Optional<TouristPlace> touristPlace = touristPlaceRepository.findById(insertDTO.getTouristPlaceId());
        if (touristPlace.isEmpty()) {
            return Result.failure("tourist place not found");
        }

        if (insertDTO.getPrice() > 1000000 || insertDTO.getPrice() < 10) {
            return Result.failure("price is out of range. Range accepted between 100 and 1000000");
        }

        return Result.success(touristPlace.get());
    }

    @Override
    public ActivityDTO create(ActivityInsertDTO insertDTO) {
        Activity activity = activityMapper.DTOToEntity(insertDTO);

        activity.setTouristPlace(insertDTO.getTouristPlace());

        activityRepository.saveAndFlush(activity);

        return activityMapper.entityToDTO(activity);
    }

    @Override
    public ActivityDTO getById(Long id) {
        Optional<Activity> optionalActivity = activityRepository.findById(id);
        return optionalActivity
                .map(activityMapper::entityToDTO)
                .orElse(null);

    }

    @Override
    public List<ActivityDTO> getAll() {
        List<Activity> activities =  activityRepository.findAll();

        return activities.stream()
                .map(activityMapper::entityToDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        boolean exists = activityRepository.existsById(id);
        if (!exists) {
            throw new EntityNotFoundException("Activity not found");
        }

        activityRepository.deleteById(id);
    }
}

