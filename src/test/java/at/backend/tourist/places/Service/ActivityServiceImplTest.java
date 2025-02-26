package at.backend.tourist.places.Service;

import at.backend.tourist.places.core.Exceptions.BusinessLogicException;
import at.backend.tourist.places.core.Exceptions.ResourceNotFoundException;
import at.backend.tourist.places.modules.Activity.DTOs.ActivityDTO;
import at.backend.tourist.places.modules.Activity.DTOs.ActivityInsertDTO;
import at.backend.tourist.places.modules.Activity.Model.Activity;
import at.backend.tourist.places.modules.Activity.Repository.ActivityRepository;
import at.backend.tourist.places.modules.Activity.Service.ActivityServiceImpl;
import at.backend.tourist.places.modules.Places.Models.TouristPlace;
import at.backend.tourist.places.modules.Places.Repository.TouristPlaceRepository;
import at.backend.tourist.places.modules.Activity.AutoMappers.ActivityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceImplTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private TouristPlaceRepository touristPlaceRepository;

    @Mock
    private ActivityMapper activityMapper;

    @InjectMocks
    private ActivityServiceImpl activityService;

    private Activity activity;
    private ActivityDTO activityDTO;
    private ActivityInsertDTO activityInsertDTO;
    private TouristPlace touristPlace;

    @BeforeEach
    public void setUp() {
        touristPlace = new TouristPlace(1L, "Beautiful Beach", "A stunning beach with golden sands.", 4.5,
                "https://example.com/beach.jpg", "8 AM - 10 PM", "$$", null, null, null, null);

        activity = new Activity(1L, "Hiking Tour", "A thrilling hike through the mountains.", 25.50, "3h", touristPlace);

        activityDTO = new ActivityDTO();
        activityDTO.setId(1L);
        activityDTO.setName("Hiking Tour");
        activityDTO.setDescription("A thrilling hike through the mountains.");
        activityDTO.setPrice(25.50);
        activityDTO.setDuration("3h");
        activityDTO.setTouristPlaceId(touristPlace.getId());

        activityInsertDTO = new ActivityInsertDTO();
        activityInsertDTO.setName("Hiking Tour");
        activityInsertDTO.setDescription("A thrilling hike through the mountains.");
        activityInsertDTO.setPrice(25.50);
        activityInsertDTO.setDuration("3h");
        activityInsertDTO.setTouristPlaceId(touristPlace.getId());
    }

    @Test
    public void testGetById() {
        when(activityRepository.findById(anyLong())).thenReturn(Optional.of(activity));
        when(activityMapper.entityToDTO(activity)).thenReturn(activityDTO);

        ActivityDTO result = activityService.getById(1L);

        assertNotNull(result);
        assertEquals("Hiking Tour", result.getName());
        assertEquals(25.50, result.getPrice());
    }

    @Test
    public void testGetByIdThrowsResourceNotFoundException() {
        when(activityRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> activityService.getById(1L));
    }

    @Test
    public void testGetAll() {
        List<Activity> activities = Collections.singletonList(activity);
        when(activityRepository.findAll()).thenReturn(activities);
        when(activityMapper.entityToDTO(activity)).thenReturn(activityDTO);

        List<ActivityDTO> result = activityService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hiking Tour", result.get(0).getName());
    }

    @Test
    public void testGetByTouristPlace() {
        List<Activity> activities = Collections.singletonList(activity);
        when(touristPlaceRepository.existsById(anyLong())).thenReturn(true);
        when(activityRepository.findByTouristPlaceId(anyLong())).thenReturn(activities);
        when(activityMapper.entityToDTO(activity)).thenReturn(activityDTO);

        List<ActivityDTO> result = activityService.getByTouristPlace(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hiking Tour", result.get(0).getName());
    }

    @Test
    public void testGetByTouristPlaceThrowsResourceNotFoundException() {
        when(touristPlaceRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> activityService.getByTouristPlace(1L));
    }

    @Test
    public void testCreate() {
        when(touristPlaceRepository.findById(1L)).thenReturn(Optional.of(new TouristPlace()));
        when(activityMapper.DTOToEntity(any(ActivityInsertDTO.class))).thenReturn(activity);
        when(activityRepository.saveAndFlush(any(Activity.class))).thenReturn(activity);
        when(activityMapper.entityToDTO(any(Activity.class))).thenReturn(activityDTO);

        ActivityDTO result = activityService.create(activityInsertDTO);

        assertNotNull(result);
        assertEquals("Hiking Tour", result.getName());
    }

    @Test
    public void testCreateThrowsBusinessLogicException() {
        when(touristPlaceRepository.findById(1L)).thenReturn(Optional.of(new TouristPlace()));
        activityInsertDTO.setPrice(10000001d);  // Invalid price

        assertThrows(BusinessLogicException.class, () -> activityService.create(activityInsertDTO));
    }

    @Test
    public void testDelete() {
        when(activityRepository.findById(anyLong())).thenReturn(Optional.of(activity));

        activityService.delete(1L);

        verify(activityRepository, times(1)).delete(activity);
    }

    @Test
    public void testDeleteThrowsResourceNotFoundException() {
        when(activityRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> activityService.delete(1L));
    }
}
