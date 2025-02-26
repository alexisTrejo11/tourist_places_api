package at.backend.tourist.places.Controller;

import at.backend.tourist.places.modules.Auth.JWT.JwtService;
import at.backend.tourist.places.modules.Places.DTOs.PlaceListDTO;
import at.backend.tourist.places.modules.Places.DTOs.PlaceListInsertDTO;
import at.backend.tourist.places.modules.Places.Service.PlaceListService;
import at.backend.tourist.places.modules.User.Controller.UserPlaceListController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPlaceListControllerTest {

    @Mock
    private PlaceListService placeListService;

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserPlaceListController userPlaceListController;

    private Long userId;
    private PlaceListDTO sampleList;

    @BeforeEach
    void setUp() {
        userId = 1L;
        sampleList = new PlaceListDTO();
        sampleList.setId(100L);
    }

    @Test
    void testNewList() {
        PlaceListInsertDTO insertDTO = new PlaceListInsertDTO();
        when(jwtService.getIdFromRequest(request)).thenReturn(userId);
        when(placeListService.create(any(PlaceListInsertDTO.class))).thenReturn(sampleList);

        ResponseEntity<PlaceListDTO> response = userPlaceListController.newList(insertDTO, request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleList, response.getBody());
    }

    @Test
    void testGetMyLists() {
        when(jwtService.getIdFromRequest(request)).thenReturn(userId);
        when(placeListService.getByUserId(userId)).thenReturn(List.of(sampleList));

        ResponseEntity<List<PlaceListDTO>> response = userPlaceListController.getMyLists(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testAddPlaces() {
        Long placeListId = 100L;
        Set<Long> placeIds = Set.of(1L, 2L);
        when(placeListService.addPlaces(placeListId, placeIds)).thenReturn(sampleList);

        ResponseEntity<PlaceListDTO> response = userPlaceListController.addPlaces(placeListId, placeIds);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testRemovePlaces() {
        Long placeListId = 100L;
        Set<Long> placeIds = Set.of(1L, 2L);
        when(placeListService.removePlaces(placeListId, placeIds)).thenReturn(sampleList);

        ResponseEntity<PlaceListDTO> response = userPlaceListController.removePlaces(placeListId, placeIds);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeleteMyList() {
        Long listId = 100L;
        when(jwtService.getIdFromRequest(request)).thenReturn(userId);
        doNothing().when(placeListService).delete(listId, userId);

        ResponseEntity<Void> response = userPlaceListController.deleteMyList(listId, request);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
    }
}

