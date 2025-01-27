package at.backend.tourist.places.Controller;

import at.backend.tourist.places.DTOs.UserDTO;
import at.backend.tourist.places.DTOs.UserInsertDTO;
import at.backend.tourist.places.Service.UserService;
import at.backend.tourist.places.Utils.Enum.Role;
import at.backend.tourist.places.JWT.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @GetMapping("/me")
    private ResponseEntity<UserDTO> me(HttpServletRequest request) {
        String email = jwtService.getEmailFromRequest(request);

        UserDTO user = userService.getByEmail(email);

        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get all users", description = "Fetches a list of all registered users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden, user lacks necessary permissions (admin required)")
    })
    @GetMapping("/all")
    private ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAll();

        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by ID", description = "Fetches a user by their ID")
    @Parameter(name = "userId", description = "ID of the user to fetch", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden, user lacks necessary permissions (admin required)"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}")
    private ResponseEntity<UserDTO> getUserById(@Valid @PathVariable Long userId) {
        UserDTO userDTO = userService.getById(userId);
        if (userDTO == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userDTO);
    }

    @Operation(summary = "Get users by role", description = "Fetches a list of users based on their role with pagination")
    @Parameter(name = "role", description = "Role of the users to fetch", required = true)
    @Parameter(name = "page", description = "Page number", example = "0")
    @Parameter(name = "size", description = "Page size", example = "10")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users with the requested role"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden, user lacks necessary permissions (admin required)")
    })
    @GetMapping("by-role/{role}")
    private ResponseEntity<List<UserDTO>> getUserByRole(@Valid @PathVariable Role role,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<UserDTO> userDTOPage = userService.getByRole(role, pageable);

        return ResponseEntity.ok(userDTOPage.get().toList());
    }

    @Operation(summary = "Create a new user", description = "Creates a new user with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid user data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden, user lacks necessary permissions (admin required)"),

    })
    @PostMapping
    private ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserInsertDTO insertDTO) {
        UserDTO userDTO = userService.create(insertDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @Operation(summary = "Update a user's role", description = "Updates the role of a user by their ID")
    @Parameter(name = "userId", description = "ID of the user to update", required = true)
    @Parameter(name = "role", description = "New role to assign to the user", required = true)
    @SecurityRequirement(name = "adminAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User role updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden, user lacks necessary permissions (admin required)"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("{userId}/update-role/{role}")
    private ResponseEntity<UserDTO> updateUserRole(@Valid @PathVariable Long userId,
                                                   @PathVariable Role role) {

        userService.updateRole(userId, role);

        return ResponseEntity.ok().build();
    }

}
