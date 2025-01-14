package at.backend.tourist.places.Service;

import at.backend.tourist.places.DTOs.UserDTO;
import at.backend.tourist.places.Utils.Result;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface AuthService {
    Result<UserDTO> processSignup(OAuth2User oAuth2User);
    Result<UserDTO> processLogin(OAuth2User oAuth2User);
}
