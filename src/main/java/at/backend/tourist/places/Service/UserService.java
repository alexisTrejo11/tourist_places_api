package at.backend.tourist.places.Service;

import at.backend.tourist.places.DTOs.SignupDTO;
import at.backend.tourist.places.DTOs.UserDTO;
import at.backend.tourist.places.DTOs.UserInsertDTO;
import at.backend.tourist.places.Utils.Enum.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService extends CommonService<UserDTO, SignupDTO> {
    Page<UserDTO> getByRole(Role role, Pageable pageable);
    UserDTO getByEmail(String email);
    UserDetails loadUserByUsername(String email);

    UserDTO create(UserInsertDTO userInsertDTO);
    void activateUser(String email);
    void updatePassword(String email, String newPassword);
    void updateRole(Long id, Role role);


}
