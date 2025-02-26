package at.backend.tourist.places.Service;

import at.backend.tourist.places.core.Exceptions.ResourceAlreadyExistsException;
import at.backend.tourist.places.core.Exceptions.ResourceNotFoundException;
import at.backend.tourist.places.modules.Country.AutoMappers.CountryMapper;
import at.backend.tourist.places.modules.Country.DTOs.CountryDTO;
import at.backend.tourist.places.modules.Country.DTOs.CountryInsertDTO;
import at.backend.tourist.places.modules.Country.Country;
import at.backend.tourist.places.modules.Country.Repository.CountryRepository;
import at.backend.tourist.places.core.Utils.Enum.Continent;
import at.backend.tourist.places.modules.Country.Service.CountryServiceImpl;
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
public class CountryServiceImplTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CountryMapper countryMapper;

    @InjectMocks
    private CountryServiceImpl countryService;

    private Country country;
    private CountryDTO countryDTO;
    private CountryInsertDTO countryInsertDTO;

    @BeforeEach
    public void setUp() {
        country = new Country(1L, "Spain", "Madrid", "Euro", "Spanish", 46719142L, 505992.0, Continent.EUROPE, "https://example.com/spain-flag.png");
        countryDTO = new CountryDTO(1L, "Spain", "Madrid", "Euro", "Spanish", 46719142L, 505992.0, Continent.EUROPE, "https://example.com/spain-flag.png");
        countryInsertDTO = new CountryInsertDTO("Spain", "Madrid", "Euro", "Spanish", 46719142L, 505992.0, Continent.EUROPE, "https://example.com/spain-flag.png");
    }

    @Test
    public void testGetById() {
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        when(countryMapper.entityToDTO(country)).thenReturn(countryDTO);

        CountryDTO result = countryService.getById(1L);

        assertNotNull(result);
        assertEquals("Spain", result.getName());
        verify(countryRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetByIdThrowsException() {
        when(countryRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> countryService.getById(1L));

        assertEquals("Country not found with id: '1'", thrown.getMessage());
    }

    @Test
    public void testGetAll() {
        when(countryRepository.findAll()).thenReturn(List.of(country));
        when(countryMapper.entityToDTO(country)).thenReturn(countryDTO);

        List<CountryDTO> result = countryService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Spain", result.get(0).getName());
    }

    @Test
    public void testGetByContinent() {
        when(countryRepository.findByContinent(Continent.EUROPE)).thenReturn(List.of(country));
        when(countryMapper.entityToDTO(country)).thenReturn(countryDTO);

        List<CountryDTO> result = countryService.getByContinent(Continent.EUROPE);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Spain", result.get(0).getName());
    }

    @Test
    public void testGetByName() {
        when(countryRepository.findByName("Spain")).thenReturn(Optional.of(country));
        when(countryMapper.entityToDTO(country)).thenReturn(countryDTO);

        CountryDTO result = countryService.getByName("Spain");

        assertNotNull(result);
        assertEquals("Spain", result.getName());
    }

    @Test
    public void testGetByNameThrowsException() {
        when(countryRepository.findByName("Spain")).thenReturn(Optional.empty());

        ResourceAlreadyExistsException thrown = assertThrows(ResourceAlreadyExistsException.class, () -> countryService.getByName("Spain"));

        assertEquals("Country already exists with name: 'Spain'", thrown.getMessage());
    }

    @Test
    public void testCreate() {
        when(countryRepository.findByName("Spain")).thenReturn(Optional.empty());
        when(countryMapper.DTOToEntity(countryInsertDTO)).thenReturn(country);
        when(countryRepository.saveAndFlush(country)).thenReturn(country);
        when(countryMapper.entityToDTO(country)).thenReturn(countryDTO);

        CountryDTO result = countryService.create(countryInsertDTO);

        assertNotNull(result);
        assertEquals("Spain", result.getName());
        verify(countryRepository, times(1)).saveAndFlush(country);
    }

    @Test
    public void testCreateThrowsException() {
        when(countryRepository.findByName("Spain")).thenReturn(Optional.of(country));

        ResourceAlreadyExistsException thrown = assertThrows(ResourceAlreadyExistsException.class, () -> countryService.create(countryInsertDTO));

        assertEquals("Country already exists with name: 'Spain'", thrown.getMessage());
    }

    @Test
    public void testDelete() {
        when(countryRepository.existsById(1L)).thenReturn(true);

        countryService.delete(1L);

        verify(countryRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteThrowsException() {
        when(countryRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> countryService.delete(1L));

        assertEquals("Country not found with id: '1'", thrown.getMessage());
    }
}
