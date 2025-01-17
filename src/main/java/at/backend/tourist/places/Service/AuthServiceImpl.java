package at.backend.tourist.places.Service;

import at.backend.tourist.places.AutoMappers.UserMappers;
import at.backend.tourist.places.DTOs.LoginDTO;
import at.backend.tourist.places.DTOs.SignupDTO;
import at.backend.tourist.places.DTOs.UserDTO;
import at.backend.tourist.places.Models.User;
import at.backend.tourist.places.Repository.UserRepository;
import at.backend.tourist.places.Utils.JWT.JwtBlacklist;
import at.backend.tourist.places.Utils.JWT.JwtUtil;
import at.backend.tourist.places.Utils.PasswordHandler;
import at.backend.tourist.places.Utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMappers userMappers;
    private final JwtUtil jwtUtil;
    private final JwtBlacklist jwtBlacklist;

    @Override
    public Result<Void> validateSignup(SignupDTO signupDTO) {
        Optional<User> user = userRepository.findByEmail(signupDTO.getEmail());
        if (user.isPresent()) {
            return Result.failure("Email already taken");
        }

        return Result.success();
    }

    @Override
    public Result<UserDTO> validateLogin(LoginDTO loginDTO) {
        Result<UserDTO> invalidLogin = Result.failure("User not found with given credentials");;

        Optional<User> user = userRepository.findByEmail(loginDTO.getEmail());
        if (user.isPresent()) {
            String hashPassword = user.get().getPassword();
            boolean isPasswordValid = PasswordHandler.validatePassword(loginDTO.getPassword(), hashPassword);

            if (!isPasswordValid) {
                return invalidLogin;
            }

            UserDTO userDTO = userMappers.entityToDTO(user.get());
            return Result.success(userDTO);
        }

        return invalidLogin;
    }

    @Override
    public String processSignup(UserDTO userDTO) {
        return jwtUtil.generateToken(userDTO.getEmail(), userDTO.getRole().toString());
    }

    @Override
    public String processLogin(UserDTO userDTO) {
        return jwtUtil.generateToken(userDTO.getEmail(), userDTO.getRole().toString());
    }

    @Override
    public void invalidToken(String token) {
        jwtBlacklist.invalidateToken(token);
    }

    @Override
    public String generateResetToken(String email) {
        return jwtUtil.generateResetToken(email);
    }

    @Override
    public String getEmailFromToken(String email) {
        return jwtUtil.getEmailFromToken(email);
    }

    @Override
    public boolean isTokenValid(String token) {
        return jwtUtil.validateToken(token);
    }
}
