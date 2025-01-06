package at.backend.tourist.places.Service;

import at.backend.tourist.places.AutoMappers.ActivityMapper;
import at.backend.tourist.places.DTOs.ActivityDTO;
import at.backend.tourist.places.DTOs.ActivityInsertDTO;
import at.backend.tourist.places.Models.Activity;
import at.backend.tourist.places.Repository.ActivityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    @Override
    public List<ActivityDTO> findByTouristPlace(Long id) {
        List<Activity> activities =  activityRepository.findByTouristPlaceId(id);

        return activities.stream()
                .map(activityMapper::entityToDTO)
                .toList();
    }

    @Override
    public ActivityDTO create(ActivityInsertDTO insertDTO) {
        Activity activity = activityMapper.DTOToEntity(insertDTO);

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

