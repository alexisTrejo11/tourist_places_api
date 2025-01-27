package at.backend.tourist.places.modules.User.Controller;

import at.backend.tourist.places.modules.Places.DTOs.PlaceListDTO;
import at.backend.tourist.places.modules.Places.DTOs.PlaceListInsertDTO;
import at.backend.tourist.places.modules.Auth.JWT.JwtService;
import at.backend.tourist.places.modules.Places.Service.PlaceListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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
            description = "Creates a new place list for the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Place list created successfully",
                    content = @Content(schema = @Schema(implementation = PlaceListDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<PlaceListDTO> newList(
            @Valid @RequestBody PlaceListInsertDTO insertDTO,
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
            description = "Retrieves all place lists for the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Place lists retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<PlaceListDTO>> getMyLists(HttpServletRequest request) {
        Long userId = jwtService.getIdFromRequest(request);
        List<PlaceListDTO> placeLists = placeListService.getByUserId(userId);
        return ResponseEntity.ok(placeLists);
    }

    @PostMapping("/{placeListId}/add-place")
    @Operation(
            summary = "Add places to a list",
            description = "Adds one or more places to an existing place list."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Places added successfully",
                    content = @Content(schema = @Schema(implementation = PlaceListDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Place list not found")
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
            description = "Removes one or more places from an existing place list."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Places removed successfully",
                    content = @Content(schema = @Schema(implementation = PlaceListDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Place list not found")
    })
    public ResponseEntity<PlaceListDTO> removePlaces(
            @Parameter(description = "ID of the place list to update", required = true)
            @PathVariable Long placeListId,
            @Parameter(description = "Set of place IDs to remove", required = true)
            @RequestBody Set<Long> placeIds
    ) {
        PlaceListDTO updatedList = placeListService.removePlaces(placeListId, placeIds);
        return ResponseEntity.ok(updatedList);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a place list",
            description = "Deletes a place list owned by the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Place list deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Place list not found")
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