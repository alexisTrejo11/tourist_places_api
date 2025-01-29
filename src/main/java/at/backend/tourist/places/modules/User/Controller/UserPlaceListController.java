package at.backend.tourist.places.modules.User.Controller;

import at.backend.tourist.places.core.SwaggerHelper.ApiResponseExamples;
import at.backend.tourist.places.modules.Places.DTOs.PlaceListDTO;
import at.backend.tourist.places.modules.Places.DTOs.PlaceListInsertDTO;
import at.backend.tourist.places.modules.Auth.JWT.JwtService;
import at.backend.tourist.places.modules.Places.Service.PlaceListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/users/lists")
@Tag(name = "User Place Lists", description = "APIs for managing user-specific place lists")
@SecurityRequirement(name = "bearerAuth")
public class UserPlaceListController {

    private final PlaceListService placeListService;
    private final JwtService jwtService;

    @PostMapping
    @Operation(
            summary = "Create a new place list",
            description = "Creates a new place list for the authenticated user. **Requires authentication**.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Place list created successfully", content = @Content(schema = @Schema(implementation = PlaceListDTO.class), examples = @ExampleObject(value = ApiResponseExamples.PLACE_LIST))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = String.class), examples = @ExampleObject(value = ApiResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = String.class), examples = @ExampleObject(value = ApiResponseExamples.UNAUTHORIZED_ACCESS)))
    })
    public ResponseEntity<PlaceListDTO> newList(@Valid @RequestBody PlaceListInsertDTO insertDTO,
                                                HttpServletRequest request
    ) {
        Long userId = jwtService.getIdFromRequest(request);
        insertDTO.setUserId(userId);
        PlaceListDTO createdList = placeListService.create(insertDTO);
        return ResponseEntity.ok(createdList);
    }

    @GetMapping("/mine")
    @Operation(
            summary = "Get my place lists",
            description = "Retrieves all place lists for the authenticated user.  **Requires authentication**.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Place lists retrieved successfully", content = @Content(schema = @Schema(implementation = List.class), examples = @ExampleObject(value = ApiResponseExamples.PLACE_LISTS))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = String.class), examples = @ExampleObject(value = ApiResponseExamples.UNAUTHORIZED_ACCESS)))
    })
    public ResponseEntity<List<PlaceListDTO>> getMyLists(HttpServletRequest request) {
        Long userId = jwtService.getIdFromRequest(request);
        List<PlaceListDTO> placeLists = placeListService.getByUserId(userId);
        return ResponseEntity.ok(placeLists);
    }

    @PostMapping("/{placeListId}/add-place")
    @Operation(
            summary = "Add places to a list",
            description = "Adds one or more places to an existing place list.  **Requires authentication**.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Places added successfully", content = @Content(schema = @Schema(implementation = PlaceListDTO.class), examples = @ExampleObject(value = ApiResponseExamples.PLACE_LISTS))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = String.class), examples = @ExampleObject(value = ApiResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = String.class), examples = @ExampleObject(value = ApiResponseExamples.UNAUTHORIZED_ACCESS))),
            @ApiResponse(responseCode = "404", description = "Place list not found", content = @Content(schema = @Schema(implementation = String.class), examples = @ExampleObject(value = ApiResponseExamples.NOT_FOUND)))
    })
    public ResponseEntity<PlaceListDTO> addPlaces(
            @Parameter(description = "ID of the place list to update", required = true)
            @PathVariable Long placeListId,
            @Parameter(description = "Set of place IDs to add", required = true)
            @RequestBody Set<Long> placeIds
    ) {
        PlaceListDTO updatedList = placeListService.addPlaces(placeListId, placeIds);
        return ResponseEntity.ok(updatedList);
    }

    @PostMapping("/{placeListId}/remove-place")
    @Operation(
            summary = "Remove places from a list",
            description = "Removes one or more places from an existing place list. **Requires authentication**.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Places removed successfully", content = @Content(schema = @Schema(implementation = PlaceListDTO.class), examples = @ExampleObject(value = ApiResponseExamples.SUCCESS))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = String.class), examples = @ExampleObject(value = ApiResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = String.class), examples = @ExampleObject(value = ApiResponseExamples.UNAUTHORIZED_ACCESS))),
            @ApiResponse(responseCode = "404", description = "Place list not found", content = @Content(schema = @Schema(implementation = String.class), examples = @ExampleObject(value = ApiResponseExamples.NOT_FOUND)))
    })
    public ResponseEntity<PlaceListDTO> removePlaces(
            @Parameter(description = "ID of the place list to update", required = true) @PathVariable Long placeListId,
            @Parameter(description = "Set of place IDs to remove", required = true)
            @RequestBody Set<Long> placeIds
    ) {
        PlaceListDTO updatedList = placeListService.removePlaces(placeListId, placeIds);
        return ResponseEntity.ok(updatedList);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a place list",
            description = "Deletes a place list owned by the authenticated user. **Requires authentication**.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Place list deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NO_CONTENT))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated", content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.UNAUTHORIZED_ACCESS))),
            @ApiResponse(responseCode = "404", description = "Place list not found", content = @Content(mediaType = "application/json", schema = @Schema(example = ApiResponseExamples.NOT_FOUND)))
    })
    public ResponseEntity<Void> deleteMyList(
            @Parameter(description = "ID of the place list to delete", required = true)
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        Long userId = jwtService.getIdFromRequest(request);
        placeListService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }
}