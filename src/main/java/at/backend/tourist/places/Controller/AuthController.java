package at.backend.tourist.places.Controller;

import at.backend.tourist.places.DTOs.LoginDTO;
import at.backend.tourist.places.DTOs.SignupDTO;
import at.backend.tourist.places.DTOs.UserDTO;
import at.backend.tourist.places.Service.AuthService;
import at.backend.tourist.places.Service.UserService;
import at.backend.tourist.places.Utils.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/api")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupDTO signupDTO) {
        Result<Void> validateSignup = authService.validateSignup(signupDTO);
        if (!validateSignup.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validateSignup.getErrorMessage());
        }

        UserDTO userDTO = userService.create(signupDTO);

        String JWT = authService.processSignup(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(JWT);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO) {
        Result<UserDTO> validateLogin = authService.validateLogin(loginDTO);
        if (!validateLogin.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validateLogin.getErrorMessage());
        }

        String JWT = authService.processLogin(validateLogin.getData());

        return ResponseEntity.ok(JWT);
    }

}
