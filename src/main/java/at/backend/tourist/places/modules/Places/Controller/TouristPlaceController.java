package at.backend.tourist.places.modules.Places.Controller;

import at.backend.tourist.places.core.Utils.Response.ResponseWrapper;
import at.backend.tourist.places.modules.Places.DTOs.TouristPlaceDTO;
import at.backend.tourist.places.modules.Places.DTOs.TouristPlaceInsertDTO;
import at.backend.tourist.places.modules.Places.DTOs.TouristPlaceSearchDTO;
import at.backend.tourist.places.modules.Places.Service.TouristPlaceService;
import at.backend.tourist.places.modules.Places.Models.PlaceRelationships;
import at.backend.tourist.places.core.Utils.Response.Result;
import at.backend.tourist.places.core.SwaggerHelper.ApiResponseExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
            @ApiResponse(responseCode = "200", description = "Tourist places found"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.UNAUTHORIZED_ACCESS))),
            @ApiResponse(responseCode = "403", description = "Forbidden, user lacks necessary permissions (admin role required)",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.FORBIDDEN)))
    })
    @GetMapping("/search")
    public ResponseWrapper<Page<TouristPlaceDTO>> searchTouristPlaces(
            @ModelAttribute TouristPlaceSearchDTO searchDto) {

        Sort sort = Sort.by(
                searchDto.getSortDirection().equalsIgnoreCase("desc") ?
                        Sort.Direction.DESC : Sort.Direction.ASC,
                searchDto.getSortBy()
        );

        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getSize(), sort);
        Page<TouristPlaceDTO> results = placeService.searchTouristPlaces(searchDto, pageable);

        return ResponseWrapper.found(results, "Tourist Places");
    }

    @Operation(summary = "Get a tourist place by ID", description = "Retrieve a tourist place by its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tourist place found"),
            @ApiResponse(responseCode = "404", description = "Tourist place not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NOT_FOUND)))
    })
    @GetMapping("/{id}")
    public ResponseWrapper<TouristPlaceDTO> getTouristPlaceById(
            @PathVariable @Schema(description = "Unique ID of the tourist place", example = "1") Long id) {

        TouristPlaceDTO place = placeService.getById(id);
        return place == null ? ResponseWrapper.notFound("Tourist Place") : ResponseWrapper.found(place, "Tourist Place");
    }

    @Operation(summary = "Get tourist places by country ID", description = "Retrieve a list of tourist places based on the country ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tourist places retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No tourist places found for the given country ID", content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NOT_FOUND)))
    })
    @GetMapping("/country/{countryId}")
    public ResponseWrapper<List<TouristPlaceDTO>> getByCountryId(
            @PathVariable @Schema(description = "Unique ID of the country", example = "10") Long countryId) {

        List<TouristPlaceDTO> places = placeService.getByCountry(countryId);
        return places.isEmpty() ? ResponseWrapper.notFound("Tourist Places for Country ID: " + countryId) : ResponseWrapper.found(places, "Tourist Places");
    }

    @Operation(summary = "Get tourist places by category ID", description = "Retrieve a list of tourist places based on the category ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tourist places retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No tourist places found for the given category ID", content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NOT_FOUND)))
    })
    @GetMapping("/category/{categoryId}")
    public ResponseWrapper<List<TouristPlaceDTO>> getByCategoryId(
            @PathVariable @Schema(description = "Unique ID of the category", example = "3") Long categoryId) {

        List<TouristPlaceDTO> places = placeService.getByCategory(categoryId);
        return places.isEmpty() ? ResponseWrapper.notFound("Tourist Places for Category ID: " + categoryId) : ResponseWrapper.found(places, "Tourist Places");
    }


    @Operation(
            summary = "Create a new tourist place",
            description = "Create a new tourist place with the provided details. **Requires ADMIN role**.",
            security = @SecurityRequirement(name = "admin")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tourist place created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.UNAUTHORIZED_ACCESS))),
            @ApiResponse(responseCode = "403", description = "Forbidden, user lacks necessary permissions (admin role required)",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.FORBIDDEN)))
    })
    @PostMapping
    public ResponseWrapper<TouristPlaceDTO> createTouristPlace(
            @Valid @RequestBody(description = "Details of the new tourist place to create",
                    required = true, content = @Content(schema = @Schema(implementation = TouristPlaceInsertDTO.class)))
            TouristPlaceInsertDTO insertDTO) {

        TouristPlaceDTO createdTouristPlace = placeService.create(insertDTO);
        return ResponseWrapper.created(createdTouristPlace, "Tourist Place");
    }

    @Operation(
            summary = "Delete a tourist place by ID",
            description = "Delete a tourist place from the system using its unique ID. **Requires ADMIN role**.",
            security = @SecurityRequirement(name = "admin")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tourist place deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Tourist place not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NOT_FOUND))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.UNAUTHORIZED_ACCESS))),
            @ApiResponse(responseCode = "403", description = "Forbidden, user lacks necessary permissions (admin role required)",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.FORBIDDEN)))
    })
    @DeleteMapping("/{id}")
    public ResponseWrapper<Void> deleteTouristPlace(
            @PathVariable @Schema(description = "Unique ID of the tourist place to delete", example = "1") Long id) {

        TouristPlaceDTO place = placeService.getById(id);
        if (place == null) {
            return ResponseWrapper.notFound("Tourist Place");
        }

        placeService.delete(id);
        return ResponseWrapper.deleted("Tourist Place");
    }
}
