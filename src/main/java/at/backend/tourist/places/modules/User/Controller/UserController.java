package at.backend.tourist.places.modules.User.Controller;

import at.backend.tourist.places.core.SwaggerHelper.ApiResponseExamples;
import at.backend.tourist.places.core.Utils.ResponseWrapper;
import at.backend.tourist.places.modules.User.DTOs.UserDTO;
import at.backend.tourist.places.modules.User.DTOs.UserInsertDTO;
import at.backend.tourist.places.modules.User.Service.UserService;
import at.backend.tourist.places.core.Utils.Enum.Role;
import at.backend.tourist.places.modules.Auth.JWT.JwtService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/api/users/admin")
@Tag(name = "User Management", description = "Operations related to user management")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    @Operation(summary = "Get current logged-in user", description = "Fetches the details of the currently logged-in user based on the JWT token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User details retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseWrapper.class),
                            examples = @ExampleObject(value = ApiResponseExamples.USER))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.UNAUTHORIZED_ACCESS)))
    })
    @GetMapping("/me")
    private ResponseEntity<ResponseWrapper<UserDTO>> me(HttpServletRequest request) {
        String email = jwtService.getEmailFromRequest(request);
        UserDTO user = userService.getByEmail(email);
        return ResponseEntity.ok(ResponseWrapper.found(user, "User"));
    }

    @Operation(summary = "Get all users", description = "Fetches a list of all registered users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of users retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseWrapper.class),
                            examples = @ExampleObject(value = ApiResponseExamples.USERS))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.UNAUTHORIZED_ACCESS))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.FORBIDDEN)))
    })
    @GetMapping("/all")
    private ResponseEntity<ResponseWrapper<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAll();
        return ResponseEntity.ok(ResponseWrapper.found(users, "Users"));
    }

    @Operation(summary = "Get user by ID", description = "Retrieves user details by their unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseWrapper.class),
                            examples = @ExampleObject(value = ApiResponseExamples.USER))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.NOT_FOUND))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.UNAUTHORIZED_ACCESS)))
    })
    @GetMapping("/{userId}")
    private ResponseEntity<ResponseWrapper<UserDTO>> getUserById(
            @Parameter(description = "ID of the user to fetch", example = "1", required = true)
            @PathVariable Long userId) {

        UserDTO userDTO = userService.getById(userId);
        return ResponseEntity.ok(ResponseWrapper.found(userDTO, "User"));
    }

    @Operation(summary = "Get users by role", description = "Fetches paginated list of users filtered by role")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseWrapper.class),
                            examples = @ExampleObject(value = ApiResponseExamples.USERS))),
            @ApiResponse(responseCode = "400", description = "Invalid role",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.UNAUTHORIZED_ACCESS)))
    })
    @GetMapping("by-role/{role}")
    private ResponseEntity<ResponseWrapper<Page<UserDTO>>> getUserByRole(
            @Parameter(description = "Role to filter by", example = "ADMIN", required = true)
            @PathVariable Role role,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> userPage = userService.getByRole(role, pageable);
        return ResponseEntity.ok(ResponseWrapper.found(userPage, "User"));
    }

    @Operation(summary = "Create a new user", description = "Registers a new user in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseWrapper.class),
                            examples = @ExampleObject(value = ApiResponseExamples.USER_CREATED))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.UNAUTHORIZED_ACCESS)))
    })
    @PostMapping
    private ResponseEntity<ResponseWrapper<UserDTO>> createUser(
            @Valid @RequestBody UserInsertDTO insertDTO) {

        UserDTO userDTO = userService.create(insertDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created(userDTO, "User"));
    }

    @Operation(summary = "Update user role", description = "Updates the role of an existing user (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.SUCCESS))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.NOT_FOUND))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.FORBIDDEN)))
    })
    @PutMapping("{userId}/update-role/{role}")
    private ResponseEntity<ResponseWrapper<Void>> updateUserRole(
            @Parameter(description = "ID of the user to update", example = "1", required = true)
            @PathVariable Long userId,
            @Parameter(description = "New role to assign", example = "ADMIN", required = true)
            @PathVariable Role role) {

        userService.updateRole(userId, role);
        return ResponseEntity.ok(ResponseWrapper.ok("User", "role update"));
    }
}