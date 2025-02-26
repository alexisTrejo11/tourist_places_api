package at.backend.tourist.places.Service;

import at.backend.tourist.places.core.Exceptions.ResourceNotFoundException;
import at.backend.tourist.places.modules.Auth.DTOs.SignupDTO;
import at.backend.tourist.places.modules.User.AutoMapper.UserMappers;
import at.backend.tourist.places.modules.User.DTOs.UserDTO;
import at.backend.tourist.places.modules.User.DTOs.UserInsertDTO;
import at.backend.tourist.places.modules.User.Model.User;
import at.backend.tourist.places.modules.User.Repository.UserRepository;
import at.backend.tourist.places.modules.User.Service.UserServiceImpl;
import at.backend.tourist.places.core.Utils.Enum.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMappers userMappers;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;
    private UserInsertDTO userInsertDTO;
    private SignupDTO signupDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(Role.VIEWER);
        user.setActivated(true);

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test@example.com");

        userInsertDTO = new UserInsertDTO();
        userInsertDTO.setEmail("test@example.com");
        userInsertDTO.setPassword("password");

        signupDTO = new SignupDTO();
        signupDTO.setEmail("test@example.com");
        signupDTO.setPassword("password");
    }

    @Test
    void testGetById_UserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMappers.entityToDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getById(1L);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testGetById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getById(1L));
    }

    @Test
    void testGetByEmail_UserExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userMappers.entityToDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testGetByEmail_UserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getByEmail("test@example.com"));
    }

    @Test
    void testCreateUser_Success() {
        when(userMappers.DTOToEntity(userInsertDTO)).thenReturn(user);
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);
        when(userMappers.entityToDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.create(userInsertDTO);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testDelete_UserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> userService.delete(1L));
    }

    @Test
    void testDelete_UserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(Exception.class, () -> userService.delete(1L));
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.loadUserByUsername("test@example.com"));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("test@example.com"));
    }

    @Test
    void testUpdateRole_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        assertDoesNotThrow(() -> userService.updateRole(1L, Role.ADMIN));
    }

    @Test
    void testUpdateRole_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateRole(1L, Role.ADMIN));
    }

    @Test
    void testActivateUser_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        assertDoesNotThrow(() -> userService.activateUser("test@example.com"));
    }

    @Test
    void testActivateUser_UserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.activateUser("test@example.com"));
    }
}

