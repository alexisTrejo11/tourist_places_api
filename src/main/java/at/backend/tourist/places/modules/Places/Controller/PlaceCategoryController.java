package at.backend.tourist.places.modules.Places.Controller;

import at.backend.tourist.places.modules.Places.DTOs.PlaceCategoryDTO;
import at.backend.tourist.places.modules.Places.DTOs.PlaceCategoryInsertDTO;
import at.backend.tourist.places.modules.Places.Service.PlaceCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlaceCategoryDTO.class)))
            }
    )
    @GetMapping
    public List<PlaceCategoryDTO> getAllCountries() {
        return placeCategoryService.getAll();
    }

    @Operation(
            summary = "Get a place category by ID",
            description = "Retrieves the details of a specific place category by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of the place category",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlaceCategoryDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Place category not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PlaceCategoryDTO> getPlaceCategoryById(@PathVariable Long id) {
        PlaceCategoryDTO country = placeCategoryService.getById(id);
        if (country == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(country);
    }

    @Operation(
            summary = "Create a new place category",
            description = "Creates a new place category with the provided details (admin role required)",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Place category created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlaceCategoryDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid place category data"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden, user lacks necessary permissions (admin role required)")
            }
    )
    @PostMapping
    public ResponseEntity<PlaceCategoryDTO> createPlaceCategory(@RequestBody PlaceCategoryInsertDTO insertDTO) {
        PlaceCategoryDTO createdPlaceCategory = placeCategoryService.create(insertDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlaceCategory);
    }

    @Operation(
            summary = "Delete a place category",
            description = "Deletes a specific place category by its ID (admin role required)",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Place category deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Place category not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden, user lacks necessary permissions (admin role required)")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaceCategory(@PathVariable Long id) {
        if (placeCategoryService.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        placeCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
