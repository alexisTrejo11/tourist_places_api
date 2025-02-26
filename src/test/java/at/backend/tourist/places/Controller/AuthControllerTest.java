package at.backend.tourist.places.Controller;


import at.backend.tourist.places.modules.Auth.Controller.AuthController;
import at.backend.tourist.places.modules.Auth.DTOs.LoginDTO;
import at.backend.tourist.places.modules.Auth.DTOs.LoginResponseDTO;
import at.backend.tourist.places.modules.Auth.DTOs.SignupDTO;
import at.backend.tourist.places.modules.User.DTOs.UserDTO;
import at.backend.tourist.places.modules.Auth.Service.AuthService;
import at.backend.tourist.places.modules.User.Service.UserService;
import at.backend.tourist.places.core.Service.SendingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private SendingService sendingService;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void signup_ShouldReturn201_WhenValidSignup() throws Exception {
        SignupDTO signupDTO = new SignupDTO("TestUser", "test@example.com", "Password123!");

        Mockito.doNothing().when(authService).validateSignup(any(SignupDTO.class));
        when(userService.create(any(SignupDTO.class))).thenReturn(new UserDTO( "TestUser", "test@example.com",""));

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("An Email will be sent to the email provided. Use that token to activate your account."));
    }

    @Test
    void login_ShouldReturn200_WhenValidCredentials() throws Exception {
        LoginDTO loginDTO = new LoginDTO("test@example.com", "password123");
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9");

        when(authService.validateLogin(any(LoginDTO.class))).thenReturn(new UserDTO("test@example.com", "TestUser", ""));
        when(authService.processLogin(any(UserDTO.class))).thenReturn(loginResponseDTO);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"));
    }

    @Test
    void logout_ShouldReturn200_WhenValidToken() throws Exception {
        String token = "Bearer jwt_token_example";

        mockMvc.perform(post("/auth/logout")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout Successfully"));
    }
}

