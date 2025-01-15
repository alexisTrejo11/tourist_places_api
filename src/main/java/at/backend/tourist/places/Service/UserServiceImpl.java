package at.backend.tourist.places.Service;

import at.backend.tourist.places.AutoMappers.UserMappers;
import at.backend.tourist.places.DTOs.SignupDTO;
import at.backend.tourist.places.DTOs.UserDTO;
import at.backend.tourist.places.Models.User;
import at.backend.tourist.places.Repository.UserRepository;
import at.backend.tourist.places.Utils.PasswordHandler;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMappers userMappers;

    @Override
    public UserDTO create(SignupDTO signupDTO) {
       User user = userMappers.DTOToEntity(signupDTO);

       String hashedPassword = PasswordHandler.hashPassword(user.getPassword());
       user.setPassword(hashedPassword);

       userRepository.saveAndFlush(user);

       return userMappers.entityToDTO(user);
    }

    @Override
    public UserDTO getById(Long id) {
        Optional<User> user = userRepository.findById(id);

        return user.map(userMappers::entityToDTO).orElse(null);
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMappers::entityToDTO)
                .toList();
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
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}
