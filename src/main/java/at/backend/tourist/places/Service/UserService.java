package at.backend.tourist.places.Service;

import at.backend.tourist.places.DTOs.SignupDTO;
import at.backend.tourist.places.DTOs.UserDTO;
import at.backend.tourist.places.DTOs.UserInsertDTO;
import at.backend.tourist.places.Utils.Result;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService extends CommonService<UserDTO, SignupDTO> {
    UserDTO create(UserInsertDTO userInsertDTO);
    UserDetails loadUserByUsername(String email);
    void updatePassword(String email, String newPassword);
    void activateUser(String email);
}
