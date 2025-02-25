package at.backend.tourist.places.modules.Activity.Service;

import at.backend.tourist.places.core.Exceptions.BusinessLogicException;
import at.backend.tourist.places.core.Exceptions.ResourceNotFoundException;
import at.backend.tourist.places.modules.Activity.AutoMappers.ActivityMapper;
import at.backend.tourist.places.modules.Activity.DTOs.ActivityDTO;
import at.backend.tourist.places.modules.Activity.DTOs.ActivityInsertDTO;
import at.backend.tourist.places.modules.Activity.Model.Activity;
import at.backend.tourist.places.modules.Places.Models.TouristPlace;
import at.backend.tourist.places.modules.Activity.Repository.ActivityRepository;
import at.backend.tourist.places.modules.Places.Repository.TouristPlaceRepository;
import at.backend.tourist.places.core.Utils.Response.Result;
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
        return activityRepository.findById(id)
                .map(activityMapper::entityToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", "id", id));
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
        if (!touristPlaceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tourist place", "id", id);
        }

        return activityRepository.findByTouristPlaceId(id).stream()
                .map(activityMapper::entityToDTO)
                .toList();
    }

    private void validate(ActivityInsertDTO insertDTO) {
        touristPlaceRepository.findById(insertDTO.getTouristPlaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Tourist place", "id", insertDTO.getTouristPlaceId()));

        if (insertDTO.getPrice() > 1000000 || insertDTO.getPrice() < 10) {
            throw new BusinessLogicException("Price must be between 10 and 1,000,000");
        }
    }

    @Override
    public ActivityDTO create(ActivityInsertDTO insertDTO) {
        log.info("Starting to create activity for tourist place: {}", insertDTO.getTouristPlaceId());

        validate(insertDTO);

        Activity activity = activityMapper.DTOToEntity(insertDTO);
        activity.setTouristPlace(touristPlaceRepository.getReferenceById(insertDTO.getTouristPlaceId()));

        activityRepository.saveAndFlush(activity);
        log.info("Activity created successfully with ID: {}", activity.getId());

        return activityMapper.entityToDTO(activity);
    }

    @Override
    public void delete(Long id) {
        log.info("Attempting to delete activity with ID: {}", id);

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", "id", id));

        activityRepository.delete(activity);
        log.info("Activity with ID: {} deleted successfully", id);
    }
}