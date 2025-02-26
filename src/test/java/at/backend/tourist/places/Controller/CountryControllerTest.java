package at.backend.tourist.places.Controller;

import at.backend.tourist.places.core.Utils.Enum.Continent;
import at.backend.tourist.places.modules.Auth.JWT.JwtAuthenticationFilter;
import at.backend.tourist.places.modules.Auth.JWT.JwtService;
import at.backend.tourist.places.modules.Country.Controller.CountryController;
import at.backend.tourist.places.modules.Country.DTOs.CountryDTO;
import at.backend.tourist.places.modules.Country.DTOs.CountryInsertDTO;
import at.backend.tourist.places.modules.Country.Service.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Autowired
    private ObjectMapper objectMapper;

    private CountryDTO countryDTO;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        countryDTO = new CountryDTO(1L, "Spain", "Madrid", "Euro", "Spanish", 46719142L, 505992.0, Continent.EUROPE, "https://example.com/spain-flag.png");
    }

    @Test
    void testGetCountryById() throws Exception {
        when(countryService.getById(1L)).thenReturn(countryDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/countries/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Spain"));
    }

    @Test
    void testGetCountryByName() throws Exception {
        when(countryService.getByName("Spain")).thenReturn(countryDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/countries/name/Spain"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Spain"));  // Corrected expected value
    }

    @Test
    void testGetAllCountries() throws Exception {
        List<CountryDTO> countries = Arrays.asList(countryDTO, new CountryDTO(2L, "France", "Paris", "Euro", "French", 46719142L, 505992.0, Continent.EUROPE, "https://example.com/france-flag.png"));
        when(countryService.getAll()).thenReturn(countries);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void testCreateCountry() throws Exception {
        CountryInsertDTO insertDTO = new CountryInsertDTO("Spain", "Madrid", "Euro", "Spanish", 46719142L, 505992.0, Continent.EUROPE, "https://example.com/spain-flag.png");
        when(countryService.create(any(CountryInsertDTO.class))).thenReturn(countryDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/api/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(insertDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Spain"));
    }

    @Test
    void testDeleteCountry() throws Exception {
        Mockito.doNothing().when(countryService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/api/countries/1"))
                .andExpect(status().isNoContent());
    }
}
