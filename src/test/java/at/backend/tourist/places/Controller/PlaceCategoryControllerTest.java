package at.backend.tourist.places.Controller;


import at.backend.tourist.places.core.Utils.Response.ResponseWrapper;
import at.backend.tourist.places.modules.Places.Controller.PlaceCategoryController;
import at.backend.tourist.places.modules.Places.DTOs.PlaceCategoryDTO;
import at.backend.tourist.places.modules.Places.DTOs.PlaceCategoryInsertDTO;
import at.backend.tourist.places.modules.Places.Service.PlaceCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaceCategoryControllerTest {

    @InjectMocks
    private PlaceCategoryController placeCategoryController;

    @Mock
    private PlaceCategoryService placeCategoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPlaceCategories() {
        List<PlaceCategoryDTO> mockCategories = Arrays.asList(
                new PlaceCategoryDTO(1L, "Historical", "Test Description"),
                new PlaceCategoryDTO(2L, "Natural", "Test Description")
        );
        when(placeCategoryService.getAll()).thenReturn(mockCategories);

        ResponseWrapper<List<PlaceCategoryDTO>> response = placeCategoryController.getAllPlaceCategories();

        assertNotNull(response);
        assertEquals(2, response.getData().size());
        assertEquals("Place Categories data successfully fetched", response.getMessage());
    }

    @Test
    void testGetPlaceCategoryByIdFound() {
        PlaceCategoryDTO mockCategory = new PlaceCategoryDTO(1L, "Historical", "Test Description");
        when(placeCategoryService.getById(1L)).thenReturn(mockCategory);

        ResponseWrapper<PlaceCategoryDTO> response = placeCategoryController.getPlaceCategoryById(1L);

        assertNotNull(response);
        assertEquals("Historical", response.getData().getName());
    }

    @Test
    void testGetPlaceCategoryByIdNotFound() {
        when(placeCategoryService.getById(99L)).thenReturn(null);

        ResponseWrapper<PlaceCategoryDTO> response = placeCategoryController.getPlaceCategoryById(99L);

        assertNotNull(response);
        assertEquals("Place Category not found", response.getMessage());
    }

    @Test
    void testCreatePlaceCategory() {
        PlaceCategoryInsertDTO insertDTO = new PlaceCategoryInsertDTO("Cultural", "Test Description");
        PlaceCategoryDTO mockCategory = new PlaceCategoryDTO(3L, "Cultural", "Test Description");
        when(placeCategoryService.create(insertDTO)).thenReturn(mockCategory);

        ResponseWrapper<PlaceCategoryDTO> response = placeCategoryController.createPlaceCategory(insertDTO);

        assertNotNull(response);
        assertEquals("Cultural", response.getData().getName());
    }

    @Test
    void testDeletePlaceCategorySuccess() {
        when(placeCategoryService.getById(1L)).thenReturn(new PlaceCategoryDTO(1L, "Historical", "Test Description"));
        doNothing().when(placeCategoryService).delete(1L);

        ResponseWrapper<Void> response = placeCategoryController.deletePlaceCategory(1L);

        assertNotNull(response);
        assertEquals("Place Category successfully deleted", response.getMessage());
    }

    @Test
    void testDeletePlaceCategoryNotFound() {
        when(placeCategoryService.getById(99L)).thenReturn(null);

        ResponseWrapper<Void> response = placeCategoryController.deletePlaceCategory(99L);

        assertNotNull(response);
        assertEquals("Place Category not found", response.getMessage());
    }
}
