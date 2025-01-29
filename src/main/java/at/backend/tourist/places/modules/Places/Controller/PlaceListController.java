package at.backend.tourist.places.modules.Places.Controller;

import at.backend.tourist.places.core.Utils.ResponseWrapper;
import at.backend.tourist.places.modules.Places.DTOs.PlaceListDTO;
import at.backend.tourist.places.modules.Places.Service.PlaceListService;
import at.backend.tourist.places.core.SwaggerHelper.ApiResponseExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/place-lists")
@RequiredArgsConstructor
public class PlaceListController {

    private final PlaceListService placeListService;

    @Operation(
            summary = "Get place list by ID",
            description = "Retrieves a place list by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of place list",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.USER_FOUND))),
                    @ApiResponse(responseCode = "404", description = "Place list not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NOT_FOUND)))
            }
    )
    @GetMapping("/{id}")
    public ResponseWrapper<PlaceListDTO> getById(@PathVariable Long id) {
        PlaceListDTO placeList = placeListService.getById(id);
        if (placeList == null) {
            return ResponseWrapper.notFound("Place List");
        }
        return ResponseWrapper.found(placeList, "Place List");
    }

    @Operation(
            summary = "Get all place lists",
            description = "Retrieves all available place lists",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of place lists",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.USER_FOUND))),
                    @ApiResponse(responseCode = "404", description = "No place lists found",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NOT_FOUND)))
            }
    )
    @GetMapping
    public ResponseWrapper<List<PlaceListDTO>> getAll() {
        List<PlaceListDTO> placeLists = placeListService.getAll();
        return ResponseWrapper.found(placeLists, "Place Lists");
    }

    @Operation(
            summary = "Get place lists by user ID",
            description = "Retrieves place lists for a specific user by their ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of place lists",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.USER_FOUND))),
                    @ApiResponse(responseCode = "404", description = "Place lists not found for the user",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NOT_FOUND)))
            }
    )
    @GetMapping("/user/{userId}")
    public ResponseWrapper<List<PlaceListDTO>> getByUserId(@PathVariable Long userId) {
        List<PlaceListDTO> placeLists = placeListService.getByUserId(userId);
        if (placeLists.isEmpty()) {
            return ResponseWrapper.notFound("Place Lists for User ID: " + userId);
        }
        return ResponseWrapper.found(placeLists, "Place Lists");
    }

    @Operation(
            summary = "Delete place list by ID",
            description = "Deletes a specific place list by its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successful deletion of place list",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NO_CONTENT))),
                    @ApiResponse(responseCode = "404", description = "Place list not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NOT_FOUND)))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseWrapper<Void> delete(@PathVariable Long id) {
        PlaceListDTO placeList = placeListService.getById(id);
        if (placeList == null) {
            return ResponseWrapper.notFound("Place List");
        }
        placeListService.delete(id);
        return ResponseWrapper.deleted("Place List");
    }
}
