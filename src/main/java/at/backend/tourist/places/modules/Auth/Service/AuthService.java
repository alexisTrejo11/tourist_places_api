package at.backend.tourist.places.modules.Auth.Service;

import at.backend.tourist.places.modules.Auth.DTOs.LoginDTO;
import at.backend.tourist.places.modules.Auth.DTOs.LoginResponseDTO;
import at.backend.tourist.places.modules.Auth.DTOs.SignupDTO;
import at.backend.tourist.places.modules.User.DTOs.UserDTO;
import at.backend.tourist.places.core.Utils.Result;

public interface AuthService {
    Result<Void> validateSignup(SignupDTO signupDTO);
    Result<UserDTO> validateLogin(LoginDTO signupDTO);
    Result<Void> validatePasswordFormat(String requestPassword);

    void processSignup(UserDTO signupDTO);
    LoginResponseDTO processLogin(UserDTO loginDTO);

    void invalidToken(String token);
    String processResetPassword(String email);
    String getEmailFromToken(String token);
    boolean isTokenValid(String token);
}
