package at.backend.tourist.places.Service;

import at.backend.tourist.places.DTOs.SignupDTO;
import at.backend.tourist.places.DTOs.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService extends CommonService<UserDTO, SignupDTO> {
    UserDetails loadUserByUsername(String email);
    void updatePassword(String email, String newPassword);
}
