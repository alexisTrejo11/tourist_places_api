package at.backend.tourist.places.Controller;

import at.backend.tourist.places.DTOs.LoginDTO;
import at.backend.tourist.places.DTOs.SignupDTO;
import at.backend.tourist.places.DTOs.UserDTO;
import at.backend.tourist.places.Models.User;
import at.backend.tourist.places.Repository.UserRepository;
import at.backend.tourist.places.Service.AuthService;
import at.backend.tourist.places.Service.UserService;
import at.backend.tourist.places.Utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/api")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private UserRepository userRepository;

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

    // OAUTH2

    @GetMapping("oauth2/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("oauth2/signup")
    public String signup(@ModelAttribute User user) {
        user.setProvider("local");
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("oauth2/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/oauth2/error")
    public Result<String> oauthError(HttpServletRequest request) {
        OAuth2AuthenticationException exception =
                (OAuth2AuthenticationException) request.getAttribute("org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationException");
        if (exception != null) {
            return Result.failure("OAuth2 Authentication failed: " + exception.getMessage());
        }
        return Result.failure("OAuth2 error occurred");
    }
}
