package at.backend.tourist.places.modules.User.Service;

import at.backend.tourist.places.core.Exceptions.ResourceNotFoundException;
import at.backend.tourist.places.modules.User.AutoMapper.UserMappers;
import at.backend.tourist.places.modules.Auth.DTOs.SignupDTO;
import at.backend.tourist.places.modules.User.DTOs.UserDTO;
import at.backend.tourist.places.modules.User.DTOs.UserInsertDTO;
import at.backend.tourist.places.modules.User.Model.User;
import at.backend.tourist.places.modules.User.Repository.UserRepository;
import at.backend.tourist.places.core.Utils.Enum.Role;
import at.backend.tourist.places.core.Utils.User.CustomOAuth2User;
import at.backend.tourist.places.core.Utils.User.PasswordHandler;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMappers userMappers;

    @Override
    public UserDTO getById(Long id) {
        Optional<User> user = userRepository.findById(id);

        return user.map(userMappers::entityToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Override
    public Page<UserDTO> getByRole(Role role, Pageable pageable) {
        Page<User> userPage =  userRepository.findByRole(role, pageable);
        return userPage.map(userMappers::entityToDTO);
    }

    @Override
    public UserDTO getByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(userMappers::entityToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMappers::entityToDTO)
                .toList();
    }

    @Override
    public UserDTO create(SignupDTO signupDTO) {
       User user = userMappers.DTOToEntity(signupDTO);

       String hashedPassword = PasswordHandler.hashPassword(user.getPassword());
       user.setPassword(hashedPassword);
       user.setActivated(false);

       userRepository.saveAndFlush(user);

       return userMappers.entityToDTO(user);
    }



    @Override
    public UserDTO create(UserInsertDTO insertDTO) {
        User user = userMappers.DTOToEntity(insertDTO);

        String hashedPassword = PasswordHandler.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        user.setActivated(true);

        userRepository.saveAndFlush(user);

        return userMappers.entityToDTO(user);
    }

    @Override
    public void delete(Long id) {
        boolean isUserExisting = userRepository.existsById(id);
        if (!isUserExisting) {
            throw new EntityNotFoundException("User not found");
        }

        userRepository.deleteById(id);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        if (user.getPassword() == null) {
            return new CustomOAuth2User(user.getEmail(), user.getRole());
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }


    @Override
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        String hashedPassword = PasswordHandler.hashPassword(newPassword);
        user.setPassword(hashedPassword);

        userRepository.save(user);
    }

    @Override
    public void updateRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.setRole(role);

        userRepository.save(user);
    }

    @Override
    public void activateUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        user.setActivated(true);

        userRepository.save(user);
    }
}
