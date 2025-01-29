package at.backend.tourist.places.modules.Activity.Controller;

import at.backend.tourist.places.core.SwaggerHelper.ApiResponseExamples;
import at.backend.tourist.places.core.Utils.SwaggerHelper.ApiConstants;
import at.backend.tourist.places.core.Utils.SwaggerHelper.CommonActivityResponses;
import at.backend.tourist.places.modules.Activity.DTOs.ActivityDTO;
import at.backend.tourist.places.modules.Activity.DTOs.ActivityInsertDTO;
import at.backend.tourist.places.modules.Places.TouristPlace;
import at.backend.tourist.places.modules.Activity.Service.ActivityService;
import at.backend.tourist.places.core.Utils.ResponseWrapper;
import at.backend.tourist.places.core.Utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
    @ApiResponse(responseCode = "200", description = ApiConstants.ACTIVITIES_RETRIEVED, content = @Content(schema = @Schema(implementation = ActivityDTO.class)))
    @ApiResponse(responseCode = "404", description = "No activities found", content = @Content(schema = @Schema(example = ApiResponseExamples.NOT_FOUND)))
    @GetMapping
    public ResponseEntity<ResponseWrapper<List<ActivityDTO>>> getAllActivities() {
        List<ActivityDTO> activities = activityService.getAll();
        if (activities.isEmpty()) {
            return ResponseEntity.status(404).body(ResponseWrapper.notFound("Activities"));
        }

        return ResponseEntity.ok(ResponseWrapper.found(activities, "Activities"));
    }

    @Operation(summary = "Get an activity by ID", description = "Retrieve the details of a specific activity using its ID.")
    @CommonActivityResponses
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ApiConstants.ACTIVITY_RETRIEVED, content = @Content(schema = @Schema(implementation = ActivityDTO.class))),
            @ApiResponse(responseCode = "404", description = "Activity not found", content = @Content(schema = @Schema(example = ApiResponseExamples.NOT_FOUND))),
            @ApiResponse(responseCode = "403", description = "Access forbidden, insufficient permissions", content = @Content(schema = @Schema(example = ApiResponseExamples.FORBIDDEN)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<ActivityDTO>> getActivityById(
            @Parameter(description = "ID of the activity to retrieve", example = "1") @PathVariable Long id) {
        ActivityDTO activity = activityService.getById(id);
        if (activity == null) {
            return ResponseEntity.status(404).body(ResponseWrapper.notFound("Activity"));
        }

        return ResponseEntity.ok(ResponseWrapper.found(activity, "Activity"));
    }

    @Operation(summary = "Get activities by tourist place ID", description = "Retrieve a list of activities associated with a specific tourist place.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ApiConstants.ACTIVITIES_RETRIEVED, content = @Content(schema = @Schema(example = ApiResponseExamples.ACTIVITY))),
            @ApiResponse(responseCode = "404", description = "Activities not found", content = @Content(schema = @Schema(example = ApiResponseExamples.NOT_FOUND))),
            @ApiResponse(responseCode = "403", description = "Forbidden access to the activities of this tourist place", content = @Content(schema = @Schema(example = ApiResponseExamples.FORBIDDEN)))
    })
    @GetMapping("/tourist_place/{place_id}")
    public ResponseEntity<ResponseWrapper<List<ActivityDTO>>> getByTouristPlaceId(
            @Parameter(description = "ID of the tourist place", example = "101") @PathVariable Long place_id) {
        List<ActivityDTO> activities = activityService.getByTouristPlace(place_id);
        if (activities == null || activities.isEmpty()) {
            return ResponseEntity.status(404).body(ResponseWrapper.notFound("Activities"));
        }

        return ResponseEntity.ok(ResponseWrapper.found(activities, "Activities"));
    }

    @Operation(summary = "Create a new activity", description = "Add a new activity to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = ApiConstants.ACTIVITY_CREATED, content = @Content(schema = @Schema(example =ApiResponseExamples.ACTIVITY_CREATED))),
            @ApiResponse(responseCode = "400", description = ApiConstants.INVALID_INPUT_DATA, content = @Content(schema = @Schema(example = ApiResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "403", description = "Forbidden to create activity for the user", content = @Content(schema = @Schema(example = ApiResponseExamples.FORBIDDEN)))
    })
    @PostMapping
    public ResponseEntity<ResponseWrapper<ActivityDTO>> createActivity(
            @Parameter(description = "Details of the activity to create") @RequestBody ActivityInsertDTO insertDTO) {
        Result<TouristPlace> validationResult = activityService.validate(insertDTO);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(400).body(ResponseWrapper.badRequest(validationResult.getErrorMessage()));
        }

        insertDTO.setTouristPlace(validationResult.getData());
        ActivityDTO createdActivity = activityService.create(insertDTO);

        return ResponseEntity.status(201).body(ResponseWrapper.created(createdActivity, "Activity"));
    }

    @Operation(summary = "Delete an activity by ID", description = "Delete an activity from the system using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = ApiConstants.ACTIVITY_DELETED, content = @Content(schema = @Schema(example = ApiResponseExamples.SUCCESS))),
            @ApiResponse(responseCode = "404", description = ApiConstants.ACTIVITY_NOT_FOUND, content = @Content(schema = @Schema(example = ApiResponseExamples.NOT_FOUND))),
            @ApiResponse(responseCode = "403", description = "Forbidden to delete activity for the user", content = @Content(schema = @Schema(example = ApiResponseExamples.FORBIDDEN)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Void>> deleteActivity(
            @Parameter(description = "ID of the activity to delete", example = "1") @PathVariable Long id) {
        if (activityService.getById(id) == null) {
            return ResponseEntity.status(404).body(ResponseWrapper.notFound("Activity"));
        }

        activityService.delete(id);
        return ResponseEntity.status(204).body(ResponseWrapper.deleted("Activity"));
    }
}
