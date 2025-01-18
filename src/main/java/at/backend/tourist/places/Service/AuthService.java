package at.backend.tourist.places.Service;

import at.backend.tourist.places.DTOs.LoginDTO;
import at.backend.tourist.places.DTOs.SignupDTO;
import at.backend.tourist.places.DTOs.UserDTO;
import at.backend.tourist.places.Utils.Result;

public interface AuthService {
    Result<Void> validateSignup(SignupDTO signupDTO);
    Result<UserDTO> validateLogin(LoginDTO signupDTO);
    Result<Void> validatePasswordFormat(String requestPassword);


    void processSignup(UserDTO signupDTO);
    String processLogin(UserDTO loginDTO);

    void invalidToken(String token);
    String generateResetToken(String email);
    String getEmailFromToken(String email);
    boolean isTokenValid(String token);
}
