package at.backend.tourist.places.modules.Places.Controller;

import at.backend.tourist.places.core.Utils.ResponseWrapper;
import at.backend.tourist.places.modules.Places.DTOs.PlaceCategoryDTO;
import at.backend.tourist.places.modules.Places.DTOs.PlaceCategoryInsertDTO;
import at.backend.tourist.places.modules.Places.Service.PlaceCategoryService;
import at.backend.tourist.places.core.SwaggerHelper.ApiResponseExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/place_categories")
public class PlaceCategoryController {

    @Autowired
    private PlaceCategoryService placeCategoryService;

    @Operation(
            summary = "Get all place categories",
            description = "Retrieves a list of all place categories available",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of place categories",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.USER_FOUND))),
                    @ApiResponse(responseCode = "404", description = "Place categories not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NOT_FOUND)))
            }
    )
    @GetMapping
    public ResponseWrapper<List<PlaceCategoryDTO>> getAllPlaceCategories() {
        return ResponseWrapper.found(placeCategoryService.getAll(), "Place Categories");
    }

    @Operation(
            summary = "Get a place category by ID",
            description = "Retrieves the details of a specific place category by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of the place category",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.USER_FOUND))),
                    @ApiResponse(responseCode = "404", description = "Place category not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NOT_FOUND)))
            }
    )
    @GetMapping("/{id}")
    public ResponseWrapper<PlaceCategoryDTO> getPlaceCategoryById(@PathVariable Long id) {
        PlaceCategoryDTO placeCategory = placeCategoryService.getById(id);
        if (placeCategory == null) {
            return ResponseWrapper.notFound("Place Category");
        }
        return ResponseWrapper.found(placeCategory, "Place Category");
    }

    @Operation(
            summary = "Create a new place category",
            description = "Creates a new place category with the provided details (admin role required)",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Place category created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.CREATED))),
                    @ApiResponse(responseCode = "400", description = "Invalid place category data",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.BAD_REQUEST))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.UNAUTHORIZED_ACCESS))),
                    @ApiResponse(responseCode = "403", description = "Forbidden, user lacks necessary permissions (admin role required)",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.FORBIDDEN)))
            }
    )
    @PostMapping
    public ResponseWrapper<PlaceCategoryDTO> createPlaceCategory(@RequestBody PlaceCategoryInsertDTO insertDTO) {
        PlaceCategoryDTO createdPlaceCategory = placeCategoryService.create(insertDTO);
        return ResponseWrapper.created(createdPlaceCategory, "Place Category");
    }

    @Operation(
            summary = "Delete a place category",
            description = "Deletes a specific place category by its ID (admin role required)",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Place category deleted successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NO_CONTENT))),
                    @ApiResponse(responseCode = "404", description = "Place category not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NOT_FOUND))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.UNAUTHORIZED_ACCESS))),
                    @ApiResponse(responseCode = "403", description = "Forbidden, user lacks necessary permissions (admin role required)",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.FORBIDDEN)))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseWrapper<Void> deletePlaceCategory(@PathVariable Long id) {
        if (placeCategoryService.getById(id) == null) {
            return ResponseWrapper.notFound("Place Category");
        }
        placeCategoryService.delete(id);
        return ResponseWrapper.deleted("Place Category");
    }
}
