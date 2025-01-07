package at.backend.tourist.places.Controller;

import at.backend.tourist.places.DTOs.ActivityDTO;
import at.backend.tourist.places.DTOs.ActivityInsertDTO;
import at.backend.tourist.places.Models.TouristPlace;
import at.backend.tourist.places.Service.ActivityService;
import at.backend.tourist.places.Utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping
    public List<ActivityDTO> getAllActivities() {
        return activityService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityDTO> getActivityById(@PathVariable Long id) {
        ActivityDTO activity = activityService.getById(id);
        if (activity == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(activity);
    }

    @GetMapping("/tourist_place/{place_id}")
    public ResponseEntity<List<ActivityDTO>> getByTouristPlaceId(@PathVariable Long place_id) {
        List<ActivityDTO> activity = activityService.getByTouristPlace(place_id);
        if (activity == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(activity);
    }

    @PostMapping
    public ResponseEntity<?> createActivity(@RequestBody ActivityInsertDTO insertDTO) {
       Result<TouristPlace> validationResult = activityService.validate(insertDTO);
       if (!validationResult.isSuccess()) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult.getErrorMessage());
       }

       insertDTO.setTouristPlace(validationResult.getData());
       ActivityDTO createdActivity = activityService.create(insertDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdActivity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        if (activityService.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        activityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

