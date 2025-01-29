package at.backend.tourist.places.modules.Auth.Controller;

import at.backend.tourist.places.core.SwaggerHelper.ApiResponseExamples;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and account management")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final SendingService sendingService;

    @Operation(summary = "Sign up a new user", description = "Creates a new user and sends an activation email.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseWrapper.class),
                            examples = @ExampleObject(value = ApiResponseExamples.SIGNUP_SUCCESS))),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "409", description = "User already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.CONFLICT)))
    })
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

    @Operation(summary = "Login user", description = "Authenticates a user and returns login details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User logged in successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseWrapper.class),
                            examples = @ExampleObject(value = ApiResponseExamples.LOGIN_SUCCESS))),
            @ApiResponse(responseCode = "401", description = "Invalid login credentials",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.LOGIN_FAILURE)))
    })
    @PostMapping("/login")
    public ResponseWrapper<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        Result<UserDTO> validateLogin = authService.validateLogin(loginDTO);
        if (!validateLogin.isSuccess()) {
            return ResponseWrapper.badRequest(validateLogin.getErrorMessage());
        }

        LoginResponseDTO responseDTO = authService.processLogin(validateLogin.getData());
        return ResponseWrapper.ok(responseDTO, "User", "Login");
    }

    @Operation(summary = "Logout user", description = "Logs out the user by invalidating their token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully logged out",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.SUCCESS))),
            @ApiResponse(responseCode = "400", description = "Invalid token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.BAD_REQUEST)))
    })
    @PostMapping("/logout")
    public ResponseWrapper<String> logout(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            authService.invalidToken(token);
            return ResponseWrapper.success("Logout Successfully");
        }
        return ResponseWrapper.badRequest("Invalid Token");
    }

    @Operation(summary = "Activate user account", description = "Activates a user account using the provided token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account successfully activated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.SUCCESS))),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.BAD_REQUEST)))
    })
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

    @Operation(summary = "Request password reset", description = "Sends a password reset token to the user's email.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password reset token sent",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.SUCCESS))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.NOT_FOUND)))
    })
    @PostMapping("{email}/reset-password-request")
    public ResponseEntity<ResponseWrapper<String>> resetPasswordRequest(@PathVariable String email) {
        UserDetails user = userService.loadUserByUsername(email);
        String resetToken = authService.processResetPassword(email);

        EmailSendingDTO sendingDTO = EmailSendingDTO.generatePasswordTokenDTO(email, resetToken);
        sendingService.sendEmail(sendingDTO);

        return ResponseEntity.ok(ResponseWrapper.success("A token will be sent to your email to allow password change"));
    }

    @Operation(summary = "Reset password", description = "Resets the user's password using the provided token and new password.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password successfully reset",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.SUCCESS))),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ApiResponseExamples.BAD_REQUEST)))
    })
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