package at.backend.tourist.places.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import at.backend.tourist.places.core.Exceptions.*;
import at.backend.tourist.places.modules.Auth.DTOs.*;
import at.backend.tourist.places.modules.Auth.JWT.JwtService;
import at.backend.tourist.places.modules.Auth.JWT.RedisTokenService;
import at.backend.tourist.places.modules.Auth.Service.AuthServiceImpl;
import at.backend.tourist.places.modules.User.AutoMapper.UserMappers;
import at.backend.tourist.places.modules.User.DTOs.UserDTO;
import at.backend.tourist.places.modules.User.Model.User;
import at.backend.tourist.places.modules.User.Repository.UserRepository;
import at.backend.tourist.places.core.Service.SendingService;
import at.backend.tourist.places.core.Utils.User.PasswordHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMappers userMappers;

    @Mock
    private JwtService jwtService;

    @Mock
    private SendingService sendingService;

    @Mock
    private RedisTokenService redisTokenService;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateSignup_ShouldThrowException_WhenEmailExists() {
        SignupDTO signupDTO = new SignupDTO("Mary Doe","existing@example.com", "Password123!");
        when(userRepository.findByEmail(signupDTO.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(ResourceAlreadyExistsException.class, () -> authService.validateSignup(signupDTO));
    }

    @Test
    void validateSignup_ShouldNotThrowException_WhenEmailIsUnique() {
        SignupDTO signupDTO = new SignupDTO("John Doe","newuser@example.com", "Password123!");
        when(userRepository.findByEmail(signupDTO.getEmail())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> authService.validateSignup(signupDTO));
    }

    @Test
    void validateLogin_ShouldThrowException_WhenUserNotFound() {
        LoginDTO loginDTO = new LoginDTO("notfound@example.com", "Password123!");
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.validateLogin(loginDTO));
    }

    @Test
    void validateLogin_ShouldThrowException_WhenUserNotActivated() {
        User user = new User();
        user.setActivated(false);
        when(userRepository.findByEmail("inactive@example.com")).thenReturn(Optional.of(user));

        LoginDTO loginDTO = new LoginDTO("inactive@example.com", "Password123!");
        assertThrows(BusinessLogicException.class, () -> authService.validateLogin(loginDTO));
    }

    @Test
    void validateLogin_ShouldThrowException_WhenPasswordIsInvalid() {
        User user = new User();
        user.setActivated(true);
        user.setPassword("hashedPassword");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        try (MockedStatic<PasswordHandler> passwordHandlerMock = Mockito.mockStatic(PasswordHandler.class)) {
            passwordHandlerMock.when(() -> PasswordHandler.validatePassword("wrongPassword", "hashedPassword"))
                    .thenReturn(false);

            LoginDTO loginDTO = new LoginDTO("user@example.com", "wrongPassword");
            assertThrows(BadRequestException.class, () -> authService.validateLogin(loginDTO));
        }
    }

    @Test
    void validateLogin_ShouldReturnUserDTO_WhenCredentialsAreValid() {
        // Preparar los datos de prueba
        User user = new User();
        user.setActivated(true);
        user.setPassword("hashedPassword");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        try (MockedStatic<PasswordHandler> passwordHandlerMock = Mockito.mockStatic(PasswordHandler.class)) {
            passwordHandlerMock.when(() -> PasswordHandler.validatePassword("correctPassword", "hashedPassword"))
                    .thenReturn(true);

            UserDTO userDTO = new UserDTO();
            when(userMappers.entityToDTO(user)).thenReturn(userDTO);

            LoginDTO loginDTO = new LoginDTO("user@example.com", "correctPassword");
            assertEquals(userDTO, authService.validateLogin(loginDTO));
        }
    }

    @Test
    void processResetPassword_ShouldGenerateAndSaveToken() {
        String email = "user@example.com";
        String resetToken = "resetToken123";
        when(jwtService.generateResetToken(email)).thenReturn(resetToken);

        String generatedToken = authService.processResetPassword(email);

        verify(redisTokenService).saveToken(resetToken, email, "valid_token", 10800);
        assertEquals(resetToken, generatedToken);
    }

    @Test
    void isTokenValid_ShouldReturnTrue_WhenTokenIsValid() {
        String token = "validToken";
        when(jwtService.validateToken(token)).thenReturn(true);

        assertTrue(authService.isTokenValid(token));
    }

    @Test
    void isTokenValid_ShouldReturnFalse_WhenTokenIsInvalid() {
        String token = "invalidToken";
        when(jwtService.validateToken(token)).thenReturn(false);

        assertFalse(authService.isTokenValid(token));
    }
}

