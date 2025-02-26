package at.backend.tourist.places.Controller;

import at.backend.tourist.places.modules.Activity.Controller.ActivityController;
import at.backend.tourist.places.modules.Activity.DTOs.ActivityDTO;
import at.backend.tourist.places.modules.Activity.DTOs.ActivityInsertDTO;
import at.backend.tourist.places.modules.Activity.Service.ActivityService;
import at.backend.tourist.places.modules.Auth.JWT.JwtAuthenticationFilter;
import at.backend.tourist.places.modules.Auth.JWT.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ActivityController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityService activityService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private ActivityDTO activityDTO1;
    private ActivityDTO activityDTO2;
    private ActivityInsertDTO activityInsertDTO;

    @BeforeEach
    void setUp() {
        activityDTO1 = new ActivityDTO();
        activityDTO1.setId(1L);
        activityDTO1.setName("Hiking");
        activityDTO1.setDescription("Mountain hiking experience");
        activityDTO1.setPrice(50.0);
        activityDTO1.setTouristPlaceId(101L);

        activityDTO2 = new ActivityDTO();
        activityDTO2.setId(2L);
        activityDTO2.setName("Kayaking");
        activityDTO2.setDescription("River kayaking adventure");
        activityDTO2.setPrice(75.0);
        activityDTO2.setTouristPlaceId(101L);

        activityInsertDTO = new ActivityInsertDTO();
        activityInsertDTO.setName("Swimming");
        activityInsertDTO.setDescription("Swimming lessons");
        activityInsertDTO.setPrice(30.0);
        activityInsertDTO.setTouristPlaceId(101L);
    }

    @Test
    void getAllActivities_ShouldReturnAllActivities() throws Exception {
        // Arrange
        List<ActivityDTO> activities = Arrays.asList(activityDTO1, activityDTO2);
        when(activityService.getAll()).thenReturn(activities);

        // Act & Assert
        mockMvc.perform(get("/v1/api/activities")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Activities data successfully fetched"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Hiking"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].name").value("Kayaking"));
    }

    @Test
    void getActivityById_ShouldReturnActivity_WhenActivityExists() throws Exception {
        // Arrange
        when(activityService.getById(1L)).thenReturn(activityDTO1);

        // Act & Assert
        mockMvc.perform(get("/v1/api/activities/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Activity data successfully fetched"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Hiking"))
                .andExpect(jsonPath("$.data.description").value("Mountain hiking experience"))
                .andExpect(jsonPath("$.data.price").value(50.0))
                .andExpect(jsonPath("$.data.tourist_place_id").value(101));
    }

    @Test
    void getByTouristPlaceId_ShouldReturnActivities_WhenPlaceExists() throws Exception {
        // Arrange
        List<ActivityDTO> activities = Arrays.asList(activityDTO1, activityDTO2);
        when(activityService.getByTouristPlace(101L)).thenReturn(activities);

        // Act & Assert
        mockMvc.perform(get("/v1/api/activities/tourist_place/101")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Activities data successfully fetched"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[1].id").value(2));
    }

    @Test
    void createActivity_ShouldReturnCreatedActivity() throws Exception {
        // Arrange
        ActivityDTO createdActivityDTO = new ActivityDTO();
        createdActivityDTO.setId(3L);
        createdActivityDTO.setName(activityInsertDTO.getName());
        createdActivityDTO.setDescription(activityInsertDTO.getDescription());
        createdActivityDTO.setPrice(activityInsertDTO.getPrice());
        createdActivityDTO.setTouristPlaceId(activityInsertDTO.getTouristPlaceId());

        when(activityService.create(any(ActivityInsertDTO.class))).thenReturn(createdActivityDTO);

        // Act & Assert
        mockMvc.perform(post("/v1/api/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activityInsertDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Activity successfully created"))
                .andExpect(jsonPath("$.data.id").value(3))
                .andExpect(jsonPath("$.data.name").value("Swimming"))
                .andExpect(jsonPath("$.data.description").value("Swimming lessons"))
                .andExpect(jsonPath("$.data.price").value(30.0))
                .andExpect(jsonPath("$.data.tourist_place_id").value(101));
    }

    @Test
    void deleteActivity_ShouldReturn204_WhenActivityDeleted() throws Exception {
        // Arrange
        doNothing().when(activityService).delete(1L);

        // Act & Assert
        mockMvc.perform(delete("/v1/api/activities/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Activity successfully deleted"));
    }
}
