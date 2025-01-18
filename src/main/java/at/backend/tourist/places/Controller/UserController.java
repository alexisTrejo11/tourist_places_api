package at.backend.tourist.places.Controller;

import at.backend.tourist.places.DTOs.UserDTO;
import at.backend.tourist.places.DTOs.UserInsertDTO;
import at.backend.tourist.places.Service.UserService;
import at.backend.tourist.places.Utils.JWT.JwtUtil;
import at.backend.tourist.places.Utils.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    /*
    @GetMapping("/me")
    private ResponseEntity<UserDTO> me() {
        String userDTO = jwtUtil.getEmailFromToken(token);
        userService.getById();
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }
     */

    @GetMapping("/{userId}")
    private ResponseEntity<UserDTO> getUserById(@Valid @PathVariable Long userId) {
        UserDTO userDTO = userService.getById(userId);
        if (userDTO == null) {
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userDTO);
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
}
