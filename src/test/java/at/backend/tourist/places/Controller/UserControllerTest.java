package at.backend.tourist.places.Controller;

import at.backend.tourist.places.core.Utils.Enum.Role;
import at.backend.tourist.places.modules.Auth.JWT.JwtService;
import at.backend.tourist.places.modules.User.Controller.UserController;
import at.backend.tourist.places.modules.User.DTOs.UserDTO;
import at.backend.tourist.places.modules.User.DTOs.UserInsertDTO;
import at.backend.tourist.places.modules.User.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetCurrentUser() throws Exception {
        String mockEmail = "test@example.com";
        UserDTO mockUser = new UserDTO();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        Mockito.when(jwtService.getEmailFromRequest(any(HttpServletRequest.class))).thenReturn(mockEmail);
        Mockito.when(userService.getByEmail(mockEmail)).thenReturn(mockUser);

        mockMvc.perform(get("/v1/api/users/admin/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(mockEmail));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        UserDTO mockUser = new UserDTO();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        mockUser.setName("John Doe");
        List<UserDTO> users = List.of(mockUser);
        Mockito.when(userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/v1/api/users/admin/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("John Doe"));
    }

    @Test
    public void testGetUserById() throws Exception {
        UserDTO mockUser = new UserDTO();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        mockUser.setName("John Doe");

        Mockito.when(userService.getById(1L)).thenReturn(mockUser);

        mockMvc.perform(get("/v1/api/users/admin/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    public void testCreateUser() throws Exception {
        UserInsertDTO userInsertDTO = new UserInsertDTO();
        userInsertDTO.setEmail("test@example.com");
        userInsertDTO.setPassword("Password123!");
        userInsertDTO.setRole(Role.VIEWER);

        UserDTO createdUser = new UserDTO();
        createdUser.setId(1L);
        createdUser.setEmail("test@example.com");
        createdUser.setName("John Doe");

        Mockito.when(userService.create(any(UserInsertDTO.class))).thenReturn(createdUser);

        mockMvc.perform(post("/v1/api/users/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInsertDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("John Doe"));
    }
}
