package at.backend.tourist.places.Controller;

import at.backend.tourist.places.core.Utils.Response.ResponseWrapper;
import at.backend.tourist.places.modules.Places.Controller.PlaceListController;
import at.backend.tourist.places.modules.Places.DTOs.PlaceListDTO;
import at.backend.tourist.places.modules.Places.Service.PlaceListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaceListControllerTest {

    @Mock
    private PlaceListService placeListService;

    @InjectMocks
    private PlaceListController placeListController;

    private PlaceListDTO mockPlaceList;

    @BeforeEach
    void setUp() {
        mockPlaceList = new PlaceListDTO();
        mockPlaceList.setId(1L);
        mockPlaceList.setName("Famous Landmarks");
    }

    @Test
    void getById_Found() {
        when(placeListService.getById(1L)).thenReturn(mockPlaceList);

        ResponseWrapper<PlaceListDTO> response = placeListController.getById(1L);

        assertNotNull(response);
        assertEquals("Place List data successfully fetched", response.getMessage());
        assertEquals(mockPlaceList, response.getData());
    }

    @Test
    void getById_NotFound() {
        when(placeListService.getById(2L)).thenReturn(null);

        ResponseWrapper<PlaceListDTO> response = placeListController.getById(2L);

        assertNull(response.getData());
        assertEquals("Place List not found", response.getMessage());
    }

    @Test
    void getAll_Success() {
        List<PlaceListDTO> placeLists = Arrays.asList(mockPlaceList, new PlaceListDTO());
        when(placeListService.getAll()).thenReturn(placeLists);

        ResponseWrapper<List<PlaceListDTO>> response = placeListController.getAll();

        assertNotNull(response);
        assertEquals(2, response.getData().size());
    }

    @Test
    void getByUserId_Found() {
        List<PlaceListDTO> userPlaceLists = Arrays.asList(mockPlaceList);
        when(placeListService.getByUserId(1L)).thenReturn(userPlaceLists);

        ResponseWrapper<List<PlaceListDTO>> response = placeListController.getByUserId(1L);

        assertNotNull(response);
        assertFalse(response.getData().isEmpty());
    }

    /* Controller need to be refactored in service
      @Test
    void getByUserId_NotFound() {
        when(placeListService.getByUserId(2L)).thenReturn(List.of());

        ResponseWrapper<List<PlaceListDTO>> response = placeListController.getByUserId(2L);

        assertNotNull(response);
        assertTrue(response.getData().isEmpty());
    }
     */

    @Test
    void delete_Success() {
        when(placeListService.getById(1L)).thenReturn(mockPlaceList);
        doNothing().when(placeListService).delete(1L);

        ResponseWrapper<Void> response = placeListController.delete(1L);

        assertNotNull(response);
        assertEquals("Place List successfully deleted", response.getMessage());
    }

    @Test
    void delete_NotFound() {
        when(placeListService.getById(2L)).thenReturn(null);

        ResponseWrapper<Void> response = placeListController.delete(2L);

        assertNotNull(response);
        assertEquals("Place List not found", response.getMessage());
    }
}

