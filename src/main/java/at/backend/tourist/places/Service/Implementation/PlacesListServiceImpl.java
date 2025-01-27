package at.backend.tourist.places.Service.Implementation;

import at.backend.tourist.places.AutoMappers.PlaceListMapper;
import at.backend.tourist.places.DTOs.PlaceListDTO;
import at.backend.tourist.places.DTOs.PlaceListInsertDTO;
import at.backend.tourist.places.Models.PlaceList;
import at.backend.tourist.places.Models.TouristPlace;
import at.backend.tourist.places.Models.User;
import at.backend.tourist.places.Repository.PlaceListRepository;
import at.backend.tourist.places.Repository.TouristPlaceRepository;
import at.backend.tourist.places.Repository.UserRepository;
import at.backend.tourist.places.Service.PlaceListService;
import at.backend.tourist.places.Utils.Result;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlacesListServiceImpl implements PlaceListService {
    
    private final PlaceListRepository placeListRepository;
    private final TouristPlaceRepository placeRepository;
    private final PlaceListMapper listMapper;
    private final UserRepository userRepository;

    @Override
    public PlaceListDTO getById(Long id) {
        Optional<PlaceList> optionalPlaceList = placeListRepository.findById(id);
        return optionalPlaceList
                .map(listMapper::entityToDTO)
                .orElse(null);

    }

    @Override
    public List<PlaceListDTO> getAll() {
        List<PlaceList> placeList =  placeListRepository.findAll();

        return placeList.stream()
                .map(listMapper::entityToDTO)
                .toList();
    }

    @Override
    public List<PlaceListDTO> getByUserId(Long userId) {
        List<PlaceList> placeLists = placeListRepository.findByUserId(userId);

        return placeLists.stream()
                .map(listMapper::entityToDTO)
                .toList();
    }


    @Transactional
    @Override
    public PlaceListDTO create(PlaceListInsertDTO insertDTO) {
        PlaceList placeList = listMapper.DTOToEntity(insertDTO);

        placeList.setPlaces(getPlaces(insertDTO.getPlacesIds()));
        placeList.setUser(getUser(insertDTO.getUserId()));

        placeListRepository.saveAndFlush(placeList);

        return listMapper.entityToDTO(placeList);
    }

    @Override
    @Transactional
    public PlaceListDTO addPlaces(Long placeListId, Set<Long> placeIds) {
        PlaceList placeList = placeListRepository.findById(placeListId)
                .orElseThrow(() -> new EntityNotFoundException("PlaceList not found"));

        Set<TouristPlace> placesToAdd = getPlaces(placeIds);

        placeList.getPlaces().addAll(placesToAdd);

        placeListRepository.saveAndFlush(placeList);

        return listMapper.entityToDTO(placeList);
    }

    @Override
    public void delete(Long id, Long userId) {
        Optional<PlaceList> placeList = placeListRepository.findByIdAndUserId(id, userId);
        if (placeList.isEmpty()) {
            throw new EntityNotFoundException("List not found");
        }

        placeListRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PlaceListDTO removePlaces(Long placeListId, Set<Long> placeIds) {
        PlaceList placeList = placeListRepository.findById(placeListId)
                .orElseThrow(() -> new EntityNotFoundException("PlaceList not found"));

        List<TouristPlace> placesToRemove = placeList.getPlaces().stream()
                .filter(place -> placeIds.contains(place.getId()))
                .toList();

        placeList.getPlaces().removeAll(placesToRemove);

        placeListRepository.saveAndFlush(placeList);

        return listMapper.entityToDTO(placeList);
    }


    @Override
    public void delete(Long id) {
        boolean exists = placeListRepository.existsById(id);
        if (!exists) {
            throw new EntityNotFoundException("PlaceList not found");
        }

        placeListRepository.deleteById(id);
    }


    public Result<Void> validate(PlaceListInsertDTO insertDTO, String email) {
        int incomingIds = insertDTO.getPlacesIds().size();
        if (incomingIds >= 100) {
            return Result.failure("Max limit of places reached. Limit = 100");
        }

        User user = userRepository.findByEmail(email).orElseThrow( () -> new EntityNotFoundException("User not Found"));
        int listCount = placeListRepository.findByUserId(user.getId()).size();
        if (listCount > 10) {
            return Result.failure("Max number of lists reached. Limit = 10");
        }

        return Result.success();
    }

    private Set<TouristPlace> getPlaces(Set<Long> idsList) {
        Set<TouristPlace> touristPlaces = placeRepository.findByIdIn(idsList);

        Set<Long> foundIds = touristPlaces.stream()
                .map(TouristPlace::getId)
                .collect(Collectors.toSet());

        List<Long> missingIds = idsList.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new EntityNotFoundException("Not found IDs for places: " + missingIds);
        }

        return touristPlaces;
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
