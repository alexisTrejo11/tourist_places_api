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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final TouristPlaceRepository touristPlaceRepository;
    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    @Override
    public ActivityDTO getById(Long id) {
        Optional<Activity> optionalActivity = activityRepository.findById(id);
        return optionalActivity
                .map(activityMapper::entityToDTO)
                .orElse(null);

    }

    @Override
    public List<ActivityDTO> getAll() {
        List<Activity> activities = activityRepository.findAll();

        return activities.stream()
                .map(activityMapper::entityToDTO)
                .toList();
    }

    @Override
    public List<ActivityDTO> getByTouristPlace(Long id) {
        boolean isPlaceExisting = touristPlaceRepository.existsById(id);
        if (!isPlaceExisting) {
            return null;
        }

        List<Activity> activities = activityRepository.findByTouristPlaceId(id);

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
        log.info("Starting to create activity for tourist place: {}", insertDTO.getTouristPlace());

        Activity activity = activityMapper.DTOToEntity(insertDTO);
        activity.setTouristPlace(insertDTO.getTouristPlace());

        activityRepository.saveAndFlush(activity);

        log.info("Activity created successfully with ID: {}", activity.getId());

        return activityMapper.entityToDTO(activity);
    }

    @Override
    public void delete(Long id) {
        log.info("Attempting to delete activity with ID: {}", id);

        boolean exists = activityRepository.existsById(id);
        if (!exists) {
            log.error("Activity with ID: {} not found for deletion", id);
            throw new EntityNotFoundException("Activity not found");
        }

        activityRepository.deleteById(id);
        log.info("Activity with ID: {} deleted successfully", id);
    }
}