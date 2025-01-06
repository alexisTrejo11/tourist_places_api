package at.backend.tourist.places.Service;

import at.backend.tourist.places.AutoMappers.TouristPlaceMapper;
import at.backend.tourist.places.DTOs.TouristPlaceDTO;
import at.backend.tourist.places.DTOs.TouristPlaceInsertDTO;
import at.backend.tourist.places.Models.TouristPlace;
import at.backend.tourist.places.Repository.TouristPlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TouristPlaceServiceImpl implements TouristPlaceService {

    private final TouristPlaceRepository touristPlaceRepository;
    private final TouristPlaceMapper touristPlaceMapper;

    @Override
    public List<TouristPlace> findByCountry(Long countryId) {
        return touristPlaceRepository.findByCountryId(countryId);
    }

    @Override
    public List<TouristPlace> findByCategory(Long categoryId) {
        return touristPlaceRepository.findByCategoryId(categoryId);
    }


    @Override
    public TouristPlaceDTO create(TouristPlaceInsertDTO insertDTO) {
        TouristPlace activity = touristPlaceMapper.DTOToEntity(insertDTO);

        touristPlaceRepository.saveAndFlush(activity);

        return touristPlaceMapper.entityToDTO(activity);
    }

    @Override
    public TouristPlaceDTO getById(Long id) {
        Optional<TouristPlace> optionalTouristPlace = touristPlaceRepository.findById(id);
        return optionalTouristPlace
                .map(touristPlaceMapper::entityToDTO)
                .orElse(null);
    }

    @Override
    public List<TouristPlaceDTO> getAll() {
        List<TouristPlace> activities =  touristPlaceRepository.findAll();

        return activities.stream()
                .map(touristPlaceMapper::entityToDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        boolean exists = touristPlaceRepository.existsById(id);
        if (!exists) {
            throw new EntityNotFoundException("TouristPlace not found");
        }

        touristPlaceRepository.deleteById(id);
    }
}
