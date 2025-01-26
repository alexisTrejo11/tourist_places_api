package at.backend.tourist.places.Controller;

import at.backend.tourist.places.DTOs.TouristPlaceDTO;
import at.backend.tourist.places.DTOs.TouristPlaceInsertDTO;
import at.backend.tourist.places.DTOs.TouristPlaceSearchDTO;
import at.backend.tourist.places.Service.TouristPlaceService;
import at.backend.tourist.places.Utils.PlaceRelationships;
import at.backend.tourist.places.Utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/tourist_places")
@Tag(name = "Tourist Places", description = "API for managing tourist places")
public class TouristPlaceController {

    @Autowired
    private TouristPlaceService placeService;

    @Operation(summary = "Search tourist places", description = "Search for tourist places based on various criteria such as name, description, rating, country, category, etc.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tourist places found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<TouristPlaceDTO>> searchTouristPlaces(
            @ModelAttribute TouristPlaceSearchDTO searchDto) {

        Sort sort = Sort.by(
                searchDto.getSortDirection().equalsIgnoreCase("desc") ?
                        Sort.Direction.DESC : Sort.Direction.ASC,
                searchDto.getSortBy()
        );

        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getSize(), sort);

        Page<TouristPlaceDTO> results = placeService.searchTouristPlaces(searchDto, pageable);

        return ResponseEntity.ok(results);
    }

    @Operation(summary = "Get a tourist place by ID", description = "Retrieve a tourist place by its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tourist place found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TouristPlaceDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tourist place not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TouristPlaceDTO> getTouristPlaceById(
            @PathVariable @Schema(description = "Unique ID of the tourist place", example = "1") Long id) {
        TouristPlaceDTO place = placeService.getById(id);
        if (place == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(place);
    }

    @Operation(summary = "Get tourist places by country ID", description = "Retrieve a list of tourist places based on the country ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tourist places retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TouristPlaceDTO.class))),
            @ApiResponse(responseCode = "404", description = "No tourist places found for the given country ID")
    })
    @GetMapping("/country/{countryId}")
    public ResponseEntity<List<TouristPlaceDTO>> getByCountryId(
            @PathVariable @Schema(description = "Unique ID of the country", example = "10") Long countryId) {
        List<TouristPlaceDTO> places = placeService.getByCountry(countryId);
        if (places == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(places);
    }

    @Operation(summary = "Get tourist places by category ID", description = "Retrieve a list of tourist places based on the category ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tourist places retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TouristPlaceDTO.class))),
            @ApiResponse(responseCode = "404", description = "No tourist places found for the given category ID")
    })
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TouristPlaceDTO>> getByCategoryId(
            @PathVariable @Schema(description = "Unique ID of the category", example = "3") Long categoryId) {
        List<TouristPlaceDTO> places = placeService.getByCategory(categoryId);
        if (places == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(places);
    }

    @Operation(summary = "Create a new tourist place", description = "Create a new tourist place with the provided details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tourist place created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TouristPlaceDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation failed")
    })
    @PostMapping
    public ResponseEntity<?> createTouristPlace(
            @Valid @RequestBody(description = "Details of the new tourist place to create",
                    required = true, content = @Content(schema = @Schema(implementation = TouristPlaceInsertDTO.class)))
            TouristPlaceInsertDTO insertDTO) {

        Result<PlaceRelationships> validationResult = placeService.validate(insertDTO);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult.getErrorMessage());
        }

        insertDTO.setPlaceRelationships(validationResult.getData());
        TouristPlaceDTO createdTouristPlace = placeService.create(insertDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTouristPlace);
    }

    @Operation(summary = "Delete a tourist place by ID", description = "Delete a tourist place from the system using its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tourist place deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Tourist place not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTouristPlace(
            @PathVariable @Schema(description = "Unique ID of the tourist place to delete", example = "1") Long id) {
        if (placeService.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        placeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}