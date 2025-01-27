package at.backend.tourist.places.modules.Activity.Controller;

import at.backend.tourist.places.modules.Activity.DTOs.ActivityDTO;
import at.backend.tourist.places.modules.Activity.DTOs.ActivityInsertDTO;
import at.backend.tourist.places.modules.Places.TouristPlace;
import at.backend.tourist.places.modules.Activity.Service.ActivityService;
import at.backend.tourist.places.core.Utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/activities")
@Tag(name = "Activities", description = "Endpoints for managing activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Operation(summary = "Get all activities", description = "Retrieve a list of all activities available.")
    @ApiResponse(responseCode = "200", description = "List of activities retrieved successfully")
    @GetMapping
    public List<ActivityDTO> getAllActivities() {
        return activityService.getAll();
    }

    @Operation(summary = "Get an activity by ID", description = "Retrieve the details of a specific activity using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ActivityDTO.class))),
            @ApiResponse(responseCode = "404", description = "Activity not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ActivityDTO> getActivityById(
            @Parameter(description = "ID of the activity to retrieve", example = "1") @PathVariable Long id) {
        ActivityDTO activity = activityService.getById(id);
        if (activity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(activity);
    }

    @Operation(summary = "Get activities by tourist place ID", description = "Retrieve a list of activities associated with a specific tourist place.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activities retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No activities found for the specified tourist place")
    })
    @GetMapping("/tourist_place/{place_id}")
    public ResponseEntity<List<ActivityDTO>> getByTouristPlaceId(
            @Parameter(description = "ID of the tourist place", example = "101") @PathVariable Long place_id) {
        List<ActivityDTO> activities = activityService.getByTouristPlace(place_id);
        if (activities == null || activities.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(activities);
    }

    @Operation(summary = "Create a new activity", description = "Add a new activity to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Activity created successfully",
                    content = @Content(schema = @Schema(implementation = ActivityDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<?> createActivity(
            @Parameter(description = "Details of the activity to create") @RequestBody ActivityInsertDTO insertDTO) {
        Result<TouristPlace> validationResult = activityService.validate(insertDTO);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult.getErrorMessage());
        }

        insertDTO.setTouristPlace(validationResult.getData());
        ActivityDTO createdActivity = activityService.create(insertDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdActivity);
    }

    @Operation(summary = "Delete an activity by ID", description = "Delete an activity from the system using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Activity deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Activity not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(
            @Parameter(description = "ID of the activity to delete", example = "1") @PathVariable Long id) {
        if (activityService.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        activityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
