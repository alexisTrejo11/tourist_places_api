package at.backend.tourist.places.Controller;

import at.backend.tourist.places.DTOs.LoginDTO;
import at.backend.tourist.places.DTOs.LoginResponseDTO;
import at.backend.tourist.places.DTOs.SignupDTO;
import at.backend.tourist.places.DTOs.UserDTO;
import at.backend.tourist.places.Service.AuthService;
import at.backend.tourist.places.Service.SendingService;
import at.backend.tourist.places.Service.UserService;
import at.backend.tourist.places.Utils.EmailSendingDTO;
import at.backend.tourist.places.Utils.User.ResetPasswordDTO;
import at.backend.tourist.places.Utils.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final SendingService sendingService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupDTO signupDTO) {
        Result<Void> validateSignup = authService.validateSignup(signupDTO);
        if (!validateSignup.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validateSignup.getErrorMessage());
        }

        UserDTO userDTO = userService.create(signupDTO);

        authService.processSignup(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("An Email will be sending the email provided. " +
                "Use that token to activate your account.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        Result<UserDTO> validateLogin = authService.validateLogin(loginDTO);
        if (!validateLogin.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validateLogin.getErrorMessage());
        }

        LoginResponseDTO responseDTO = authService.processLogin(validateLogin.getData());

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            authService.invalidToken(token);
            return ResponseEntity.ok("Logout Successfully");
        }
        return ResponseEntity.badRequest().body("Invalid Token");
    }

    @PostMapping("/activate-account/{token}")
    public ResponseEntity<String> activateAccount(@Valid @PathVariable String token) {
        if (!authService.isTokenValid(token)) {
            return ResponseEntity.badRequest().body("Invalid or Expired Token");
        }
        // CustomToken Needs Email
        String email = authService.getEmailFromToken(token);
        userService.activateUser(email);

        authService.invalidToken(token);

        return ResponseEntity.ok("Account Successfully Activated");
    }

    @PostMapping("{email}/reset-password-request")
    public ResponseEntity<String> resetPasswordRequest(@PathVariable String email) {
        UserDetails user = userService.loadUserByUsername(email);

        String resetToken = authService.processResetPassword(email);

        EmailSendingDTO sendingDTO = EmailSendingDTO.generatePasswordTokenDTO(email, resetToken);
        sendingService.sendEmail(sendingDTO);

        return ResponseEntity.ok("A token will be send to you user email to complete allow your " +
                "change of password");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordDTO dto) {
        if (!authService.isTokenValid(dto.getToken())) {
            return ResponseEntity.badRequest().body("Invalid or Expired Token");
        }

        String email = authService.getEmailFromToken(dto.getToken());

        userService.updatePassword(email, dto.getNewPassword());

        authService.invalidToken(dto.getToken());

        return ResponseEntity.ok("Password successfully changed");
    }
}
