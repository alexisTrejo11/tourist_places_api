package at.backend.tourist.places.Service;

import at.backend.tourist.places.core.Exceptions.ResourceNotFoundException;
import at.backend.tourist.places.modules.Places.AutoMappers.PlaceListMapper;
import at.backend.tourist.places.modules.Places.DTOs.PlaceListDTO;
import at.backend.tourist.places.modules.Places.DTOs.PlaceListInsertDTO;
import at.backend.tourist.places.modules.Places.Models.PlaceList;
import at.backend.tourist.places.modules.Places.Repository.PlaceListRepository;
import at.backend.tourist.places.modules.Places.Repository.TouristPlaceRepository;
import at.backend.tourist.places.modules.Places.Service.PlacesListServiceImpl;
import at.backend.tourist.places.modules.User.Model.User;
import at.backend.tourist.places.modules.User.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlacesListServiceImplTest {

    @Mock
    private PlaceListRepository placeListRepository;

    @Mock
    private TouristPlaceRepository placeRepository;

    @Mock
    private PlaceListMapper listMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PlacesListServiceImpl placeListService;

    private PlaceList placeList;
    private PlaceListDTO placeListDTO;
    private PlaceListInsertDTO placeListInsertDTO;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        placeList = new PlaceList();
        placeList.setId(1L);
        placeList.setName("Favorite Places");
        placeList.setUser(user);
        placeList.setPlaces(new HashSet<>());

        placeListDTO = new PlaceListDTO();
        placeListDTO.setId(1L);
        placeListDTO.setName("Favorite Places");
        placeListDTO.setUserId(1L);

        placeListInsertDTO = new PlaceListInsertDTO();
        placeListInsertDTO.setName("Favorite Places");
        placeListInsertDTO.setUserId(1L);
    }

    @Test
    void testGetById_Success() {
        when(placeListRepository.findById(1L)).thenReturn(Optional.of(placeList));
        when(listMapper.entityToDTO(placeList)).thenReturn(placeListDTO);

        PlaceListDTO result = placeListService.getById(1L);

        assertNotNull(result);
        assertEquals("Favorite Places", result.getName());
    }

    @Test
    void testGetById_NotFound() {
        when(placeListRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> placeListService.getById(1L));
    }

    @Test
    void testCreate_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(listMapper.DTOToEntity(any(PlaceListInsertDTO.class))).thenReturn(placeList);
        when(placeListRepository.saveAndFlush(any(PlaceList.class))).thenReturn(placeList);
        when(listMapper.entityToDTO(any(PlaceList.class))).thenReturn(placeListDTO);

        PlaceListDTO result = placeListService.create(placeListInsertDTO);

        assertNotNull(result);
        assertEquals("Favorite Places", result.getName());
        verify(placeListRepository, times(1)).saveAndFlush(any(PlaceList.class));
    }

    @Test
    void testCreate_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> placeListService.create(placeListInsertDTO));
    }

    @Test
    void testDelete_Success() {
        when(placeListRepository.existsById(1L)).thenReturn(true);

        placeListService.delete(1L);

        verify(placeListRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound() {
        when(placeListRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> placeListService.delete(1L));
    }
}
