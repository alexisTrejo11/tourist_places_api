package at.backend.tourist.places.modules.User.Service;

import at.backend.tourist.places.modules.Auth.DTOs.SignupDTO;
import at.backend.tourist.places.modules.User.DTOs.UserDTO;
import at.backend.tourist.places.modules.User.DTOs.UserInsertDTO;
import at.backend.tourist.places.core.Service.CommonService;
import at.backend.tourist.places.core.Utils.Enum.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService extends CommonService<UserDTO, SignupDTO> {
    Page<UserDTO> getByRole(Role role, Pageable pageable);
    UserDTO getByEmail(String email);
    UserDetails loadUserByUsername(String email);

    UserDTO create(UserInsertDTO userInsertDTO);
    void activateUser(String email);
    void updatePassword(String email, String newPassword);
    void updateRole(Long id, Role role);


}
