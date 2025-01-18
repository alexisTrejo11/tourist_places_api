package at.backend.tourist.places.Controller;

import at.backend.tourist.places.DTOs.UserDTO;
import at.backend.tourist.places.DTOs.UserInsertDTO;
import at.backend.tourist.places.Service.UserService;
import at.backend.tourist.places.Utils.Enum.Role;
import at.backend.tourist.places.Utils.JWT.JwtUtil;
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
@RequestMapping("v1/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/me")
    private ResponseEntity<UserDTO> me(HttpServletRequest request) {
        String email = jwtUtil.getEmailFromRequest(request);

        UserDTO user = userService.getByEmail(email);

        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/all")
    private ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAll();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    private ResponseEntity<UserDTO> getUserById(@Valid @PathVariable Long userId) {
        UserDTO userDTO = userService.getById(userId);
        if (userDTO == null) {
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("by-role/{role}")
    private ResponseEntity<List<UserDTO>> getUserByRole(@Valid @PathVariable Role role,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<UserDTO> userDTOPage = userService.getByRole(role, pageable);

        return ResponseEntity.ok(userDTOPage.get().toList());
    }

    @PostMapping
    private ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserInsertDTO insertDTO) {
        UserDTO userDTO = userService.create(insertDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @DeleteMapping("/{userId}")
    private ResponseEntity<Void> deleteUser(@Valid @PathVariable Long userId) {
        userService.delete(userId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("{userId}/update-role/{role}")
    private ResponseEntity<UserDTO> updateUserRole(@Valid @PathVariable Long userId,
                                                   @PathVariable Role role) {

        userService.updateRole(userId, role);

        return ResponseEntity.ok().build();
    }
}
