package at.backend.tourist.places.modules.Auth.Controller;

import at.backend.tourist.places.modules.Auth.DTOs.LoginDTO;
import at.backend.tourist.places.modules.Auth.DTOs.LoginResponseDTO;
import at.backend.tourist.places.modules.Auth.DTOs.SignupDTO;
import at.backend.tourist.places.modules.User.DTOs.UserDTO;
import at.backend.tourist.places.modules.Auth.Service.AuthService;
import at.backend.tourist.places.core.Service.SendingService;
import at.backend.tourist.places.modules.User.Service.UserService;
import at.backend.tourist.places.core.Utils.EmailSendingDTO;
import at.backend.tourist.places.core.Utils.User.ResetPasswordDTO;
import at.backend.tourist.places.core.Utils.ResponseWrapper;
import at.backend.tourist.places.core.Utils.Result;
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
    public ResponseWrapper<String> signup(@Valid @RequestBody SignupDTO signupDTO) {
        Result<Void> validateSignup = authService.validateSignup(signupDTO);
        if (!validateSignup.isSuccess()) {
            return ResponseWrapper.badRequest(validateSignup.getErrorMessage());
        }

        UserDTO userDTO = userService.create(signupDTO);

        authService.processSignup(userDTO);

        return ResponseWrapper.created("An Email will be sent to the email provided. Use that token to activate your account.");
    }

    @PostMapping("/login")
    public ResponseWrapper<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        Result<UserDTO> validateLogin = authService.validateLogin(loginDTO);
        if (!validateLogin.isSuccess()) {
            return ResponseWrapper.badRequest(validateLogin.getErrorMessage());
        }

        LoginResponseDTO responseDTO = authService.processLogin(validateLogin.getData());

        return ResponseWrapper.ok(responseDTO, "User", "Login");
    }

    @PostMapping("/logout")
    public ResponseWrapper<String> logout(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            authService.invalidToken(token);
            return ResponseWrapper.success("Logout Successfully");
        }
        return ResponseWrapper.badRequest("Invalid Token");
    }

    @PostMapping("/activate-account/{token}")
    public ResponseEntity<ResponseWrapper<String>> activateAccount(@Valid @PathVariable String token) {
        if (!authService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest("Invalid or Expired Token"));
        }

        String email = authService.getEmailFromToken(token);
        userService.activateUser(email);

        authService.invalidToken(token);

        return ResponseEntity.ok(ResponseWrapper.ok("Account", "activate"));
    }

    @PostMapping("{email}/reset-password-request")
    public ResponseEntity<ResponseWrapper<String>> resetPasswordRequest(@PathVariable String email) {
        UserDetails user = userService.loadUserByUsername(email);

        String resetToken = authService.processResetPassword(email);

        EmailSendingDTO sendingDTO = EmailSendingDTO.generatePasswordTokenDTO(email, resetToken);
        sendingService.sendEmail(sendingDTO);

        return ResponseEntity.ok(ResponseWrapper.success("A token will be sent to your email " +
                "to allow password change"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseWrapper<Void>> resetPassword(@Valid @RequestBody ResetPasswordDTO dto) {
        if (!authService.isTokenValid(dto.getToken())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseWrapper.badRequest("Invalid or Expired Token"));
        }

        String email = authService.getEmailFromToken(dto.getToken());

        userService.updatePassword(email, dto.getNewPassword());

        authService.invalidToken(dto.getToken());

        return ResponseEntity.ok(ResponseWrapper.ok("Password", "change"));
    }
}