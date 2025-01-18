package at.backend.tourist.places.Service.Implementation;

import at.backend.tourist.places.AutoMappers.UserMappers;
import at.backend.tourist.places.DTOs.LoginDTO;
import at.backend.tourist.places.DTOs.SignupDTO;
import at.backend.tourist.places.DTOs.UserDTO;
import at.backend.tourist.places.Models.User;
import at.backend.tourist.places.Repository.UserRepository;
import at.backend.tourist.places.Service.AuthService;
import at.backend.tourist.places.Service.SendingService;
import at.backend.tourist.places.Utils.EmailSendingDTO;
import at.backend.tourist.places.Utils.JWT.JwtBlacklist;
import at.backend.tourist.places.Utils.JWT.JwtUtil;
import at.backend.tourist.places.Utils.PasswordHandler;
import at.backend.tourist.places.Utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMappers userMappers;
    private final JwtUtil jwtUtil;
    private final JwtBlacklist jwtBlacklist;
    private final SendingService sendingService;

    @Override
    public Result<Void> validateSignup(SignupDTO signupDTO) {
        Optional<User> user = userRepository.findByEmail(signupDTO.getEmail());
        if (user.isPresent()) {
            return Result.failure("Email already taken");
        }

        Result<Void> passwordValidation = validatePasswordFormat(signupDTO.getPassword());
        if (!passwordValidation.isSuccess()) {
            return passwordValidation;
        }

        return Result.success();
    }

    @Override
    public Result<UserDTO> validateLogin(LoginDTO loginDTO) {

        Optional<User> optionalUser = userRepository.findByEmail(loginDTO.getEmail());
        if (optionalUser.isEmpty()) {
            return Result.failure("User not found with given credentials");
        }

        User user = optionalUser.get();
            if (!user.isActivated()) {
               return Result.failure("User not activated");
            }

            boolean isPasswordValid = PasswordHandler.validatePassword(loginDTO.getPassword(), user.getPassword());
            if (!isPasswordValid) {
                return Result.failure("Wrong password");
            }

            UserDTO userDTO = userMappers.entityToDTO(user);
            return Result.success(userDTO);
    }

    @Override
    @Async("taskExecutor")
    public void processSignup(UserDTO userDTO) {
        String activateToken = jwtUtil.generateActivateToken(userDTO.getEmail());
        EmailSendingDTO emailSendingDTO = EmailSendingDTO.generateActivateAccountToken(userDTO.getEmail(), activateToken);

        sendingService.sendEmail(emailSendingDTO);
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

    @Override
    public Result<Void> validatePasswordFormat(String requestPassword) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&.\\-])[A-Za-z\\d@$!%*?&.\\-]{8,}$";
        Pattern pattern = Pattern.compile(passwordPattern);

        if (requestPassword == null || !pattern.matcher(requestPassword).matches()) {
            return Result.failure("Password must be at least 8 characters long, include an uppercase letter, "
                    + "a lowercase letter, a number, and a special character. (@, $, !, %, *, ?, &, ., -)");
        }

        return Result.success();
    }
}
