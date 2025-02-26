package at.backend.tourist.places.Controller;

import at.backend.tourist.places.modules.Places.Controller.TouristPlaceController;
import at.backend.tourist.places.modules.Places.DTOs.TouristPlaceDTO;
import at.backend.tourist.places.modules.Places.DTOs.TouristPlaceInsertDTO;
import at.backend.tourist.places.modules.Places.DTOs.TouristPlaceSearchDTO;
import at.backend.tourist.places.modules.Places.Service.TouristPlaceService;
import at.backend.tourist.places.core.Utils.Response.ResponseWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TouristPlaceControllerTest {

    @InjectMocks
    private TouristPlaceController touristPlaceController;

    @Mock
    private TouristPlaceService placeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTouristPlaceById_Found() {
        TouristPlaceDTO placeDTO = new TouristPlaceDTO();
        when(placeService.getById(1L)).thenReturn(placeDTO);
        ResponseWrapper<TouristPlaceDTO> response = touristPlaceController.getTouristPlaceById(1L);
        assertNotNull(response);
        assertEquals("Tourist Place data successfully fetched", response.getMessage());
        assertEquals(placeDTO, response.getData());
    }

    @Test
    void testGetTouristPlaceById_NotFound() {
        when(placeService.getById(1L)).thenReturn(null);
        ResponseWrapper<TouristPlaceDTO> response = touristPlaceController.getTouristPlaceById(1L);
        assertNotNull(response);
        assertEquals("Tourist Place not found", response.getMessage());
    }

    @Test
    void testSearchTouristPlaces() {
        TouristPlaceSearchDTO searchDTO = new TouristPlaceSearchDTO();
        Pageable pageable = PageRequest.of(0, 10);
        Page<TouristPlaceDTO> mockPage = new PageImpl<>(Collections.emptyList());
        when(placeService.searchTouristPlaces(eq(searchDTO), any(Pageable.class))).thenReturn(mockPage);
        ResponseWrapper<Page<TouristPlaceDTO>> response = touristPlaceController.searchTouristPlaces(searchDTO);
        assertNotNull(response);
        assertEquals("Tourist Places data successfully fetched", response.getMessage());
    }

    @Test
    void testCreateTouristPlace() {
        TouristPlaceInsertDTO insertDTO = new TouristPlaceInsertDTO();
        TouristPlaceDTO createdDTO = new TouristPlaceDTO();
        when(placeService.create(insertDTO)).thenReturn(createdDTO);
        ResponseWrapper<TouristPlaceDTO> response = touristPlaceController.createTouristPlace(insertDTO);
        assertNotNull(response);
        assertEquals("Tourist Place successfully created", response.getMessage());
    }

    @Test
    void testDeleteTouristPlace_Found() {
        TouristPlaceDTO placeDTO = new TouristPlaceDTO();
        when(placeService.getById(1L)).thenReturn(placeDTO);
        doNothing().when(placeService).delete(1L);
        ResponseWrapper<Void> response = touristPlaceController.deleteTouristPlace(1L);
        assertNotNull(response);
        assertEquals("Tourist Place successfully deleted", response.getMessage());
    }

    @Test
    void testDeleteTouristPlace_NotFound() {
        when(placeService.getById(1L)).thenReturn(null);
        ResponseWrapper<Void> response = touristPlaceController.deleteTouristPlace(1L);
        assertNotNull(response);
        assertEquals("Tourist Place not found", response.getMessage());
    }
}

