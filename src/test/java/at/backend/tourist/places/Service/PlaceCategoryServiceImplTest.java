package at.backend.tourist.places.Service;


import at.backend.tourist.places.modules.Places.AutoMappers.PlaceCategoryMapper;
import at.backend.tourist.places.modules.Places.DTOs.PlaceCategoryDTO;
import at.backend.tourist.places.modules.Places.DTOs.PlaceCategoryInsertDTO;
import at.backend.tourist.places.modules.Places.Models.PlaceCategory;
import at.backend.tourist.places.modules.Places.Repository.PlaceCategoryRepository;
import at.backend.tourist.places.modules.Places.Service.PlaceCategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlaceCategoryServiceImplTest {

    @Mock
    private PlaceCategoryRepository placeCategoryRepository;

    @Mock
    private PlaceCategoryMapper placeCategoryMapper;

    @InjectMocks
    private PlaceCategoryServiceImpl placeCategoryService;

    private PlaceCategory placeCategory;
    private PlaceCategoryDTO placeCategoryDTO;
    private PlaceCategoryInsertDTO placeCategoryInsertDTO;

    @BeforeEach
    void setUp() {
        placeCategory = new PlaceCategory(1L, "Beaches", "Scenic seaside locations", null, null, null);
        placeCategoryDTO = new PlaceCategoryDTO();
        placeCategoryDTO.setId(1L);
        placeCategoryDTO.setName("Beaches");
        placeCategoryDTO.setDescription("Scenic seaside locations");

        placeCategoryInsertDTO = new PlaceCategoryInsertDTO();
        placeCategoryInsertDTO.setName("Beaches");
        placeCategoryInsertDTO.setDescription("Scenic seaside locations");
    }

    @Test
    void testCreate() {
        // Arrange
        when(placeCategoryMapper.DTOToEntity(placeCategoryInsertDTO)).thenReturn(placeCategory);
        when(placeCategoryRepository.saveAndFlush(placeCategory)).thenReturn(placeCategory);
        when(placeCategoryMapper.entityToDTO(placeCategory)).thenReturn(placeCategoryDTO);

        // Act
        PlaceCategoryDTO result = placeCategoryService.create(placeCategoryInsertDTO);

        // Assert
        assertNotNull(result);
        assertEquals(placeCategoryDTO.getName(), result.getName());
        assertEquals(placeCategoryDTO.getDescription(), result.getDescription());
        verify(placeCategoryRepository, times(1)).saveAndFlush(placeCategory);
    }

    @Test
    void testGetById() {
        // Arrange
        when(placeCategoryRepository.findById(1L)).thenReturn(Optional.of(placeCategory));
        when(placeCategoryMapper.entityToDTO(placeCategory)).thenReturn(placeCategoryDTO);

        // Act
        PlaceCategoryDTO result = placeCategoryService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(placeCategoryDTO.getId(), result.getId());
        assertEquals(placeCategoryDTO.getName(), result.getName());
        verify(placeCategoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetByIdNotFound() {
        // Arrange
        when(placeCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        PlaceCategoryDTO result = placeCategoryService.getById(1L);

        // Assert
        assertNull(result);
        verify(placeCategoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAll() {
        // Arrange
        List<PlaceCategory> categories = List.of(placeCategory);
        when(placeCategoryRepository.findAll()).thenReturn(categories);
        when(placeCategoryMapper.entityToDTO(placeCategory)).thenReturn(placeCategoryDTO);

        // Act
        List<PlaceCategoryDTO> result = placeCategoryService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(placeCategoryDTO.getName(), result.get(0).getName());
        verify(placeCategoryRepository, times(1)).findAll();
    }

    @Test
    void testDelete() {
        // Arrange
        when(placeCategoryRepository.existsById(1L)).thenReturn(true);

        // Act
        placeCategoryService.delete(1L);

        // Assert
        verify(placeCategoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        // Arrange
        when(placeCategoryRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> placeCategoryService.delete(1L));
        verify(placeCategoryRepository, times(0)).deleteById(1L);
    }
}
