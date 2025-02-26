package at.backend.tourist.places.Service;

import at.backend.tourist.places.core.Exceptions.ResourceNotFoundException;
import at.backend.tourist.places.modules.Places.AutoMappers.TouristPlaceMapper;
import at.backend.tourist.places.modules.Places.DTOs.TouristPlaceDTO;
import at.backend.tourist.places.modules.Places.DTOs.TouristPlaceInsertDTO;
import at.backend.tourist.places.modules.Places.Models.PlaceCategory;
import at.backend.tourist.places.modules.Places.Models.TouristPlace;
import at.backend.tourist.places.modules.Places.Repository.PlaceCategoryRepository;
import at.backend.tourist.places.modules.Places.Repository.TouristPlaceRepository;
import at.backend.tourist.places.modules.Country.Country;
import at.backend.tourist.places.modules.Country.Repository.CountryRepository;
import at.backend.tourist.places.modules.Places.Service.TouristPlaceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TouristPlaceServiceImplTest {

    @Mock
    private TouristPlaceRepository touristPlaceRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private PlaceCategoryRepository placeCategoryRepository;

    @Mock
    private TouristPlaceMapper touristPlaceMapper;

    @InjectMocks
    private TouristPlaceServiceImpl touristPlaceService;

    private TouristPlace touristPlace;
    private TouristPlaceDTO touristPlaceDTO;
    private TouristPlaceInsertDTO insertDTO;
    private Country country;
    private PlaceCategory category;

    @BeforeEach
    void setUp() {
        country = new Country();
        country.setId(1L);

        category = new PlaceCategory();
        category.setId(1L);

        touristPlace = new TouristPlace();
        touristPlace.setId(1L);
        touristPlace.setName("Eiffel Tower");
        touristPlace.setCountry(country);
        touristPlace.setCategory(category);

        touristPlaceDTO = new TouristPlaceDTO();
        touristPlaceDTO.setId(1L);
        touristPlaceDTO.setName("Eiffel Tower");

        insertDTO = new TouristPlaceInsertDTO();
        insertDTO.setName("Eiffel Tower");
        insertDTO.setCountryId(1L);
        insertDTO.setCategoryId(1L);
    }

    @Test
    void getById_Success() {
        when(touristPlaceRepository.findById(1L)).thenReturn(Optional.of(touristPlace));
        when(touristPlaceMapper.entityToDTO(touristPlace)).thenReturn(touristPlaceDTO);

        TouristPlaceDTO result = touristPlaceService.getById(1L);

        assertNotNull(result);
        assertEquals("Eiffel Tower", result.getName());
        verify(touristPlaceRepository, times(1)).findById(1L);
    }

    @Test
    void getById_NotFound() {
        when(touristPlaceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> touristPlaceService.getById(1L));
    }

    @Test
    void getAll_Success() {
        when(touristPlaceRepository.findAll()).thenReturn(List.of(touristPlace));
        when(touristPlaceMapper.entityToDTO(any())).thenReturn(touristPlaceDTO);

        List<TouristPlaceDTO> result = touristPlaceService.getAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void create_Success() {
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        when(placeCategoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(touristPlaceMapper.DTOToEntity(insertDTO)).thenReturn(touristPlace);
        when(touristPlaceRepository.saveAndFlush(any())).thenReturn(touristPlace);
        when(touristPlaceMapper.entityToDTO(any())).thenReturn(touristPlaceDTO);

        TouristPlaceDTO result = touristPlaceService.create(insertDTO);

        assertNotNull(result);
        assertEquals("Eiffel Tower", result.getName());
    }

    @Test
    void create_Fails_WhenCountryNotFound() {
        when(countryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> touristPlaceService.create(insertDTO));
    }

    @Test
    void delete_Success() {
        when(touristPlaceRepository.existsById(1L)).thenReturn(true);

        touristPlaceService.delete(1L);

        verify(touristPlaceRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_Fails_WhenNotFound() {
        when(touristPlaceRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> touristPlaceService.delete(1L));
    }
}
