package at.backend.tourist.places.Service;

import at.backend.tourist.places.AutoMappers.UserMappers;
import at.backend.tourist.places.DTOs.UserDTO;
import at.backend.tourist.places.Models.User;
import at.backend.tourist.places.Repository.UserRepository;
import at.backend.tourist.places.Utils.Enum.Role;
import at.backend.tourist.places.Utils.Result;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMappers userMappers;

    public AuthServiceImpl(UserRepository userRepository, UserMappers userMappers) {
        this.userRepository = userRepository;
        this.userMappers = userMappers;
    }

    @Override
    public Result<UserDTO> processSignup(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            return Result.failure("Email not provided by OAuth2 provider");
        }

        Result<Void> emailValidationResult = validateEmailUniqueness(email);
        if (!emailValidationResult.isSuccess()) {
            return Result.failure(emailValidationResult.getErrorMessage());
        }

        User newUser = createNewUser(oAuth2User);
        userRepository.save(newUser);

        // Generate session token
        String token = generateToken(newUser);

        return Result.success(new UserDTO(newUser.getEmail(), newUser.getName(), token));
    }

    @Override
    public Result<UserDTO> processLogin(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            return Result.failure("Email not provided by OAuth2 provider");
        }

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user != null) {
            // Generate session token
            String token = generateToken(user);

            return Result.success(new UserDTO(user.getEmail(), user.getName(), token));
        } else {
            return Result.failure("User not found for the given OAuth2 credentials");
        }
    }

    private Result<Void> validateEmailUniqueness(String email) {
        if (email == null || email.isEmpty()) {
            return Result.failure("Email is required");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            return Result.failure("Email is already taken");
        }

        return Result.success();
    }

    private User createNewUser(OAuth2User oAuth2User) {
        User newUser = new User();
        newUser.setEmail(oAuth2User.getAttribute("email"));
        newUser.setName(oAuth2User.getAttribute("name"));
        newUser.setRole(Role.VIEWER);

        return newUser;
    }

    private String generateToken(User user) {
        // Token generation logic (for simplicity, using a hardcoded string)
        // You can replace this with JWT generation logic
        return "Bearer " + user.getEmail() + "_token";
    }
}
