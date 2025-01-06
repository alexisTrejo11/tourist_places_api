package at.backend.tourist.places.Service;

import at.backend.tourist.places.AutoMappers.TouristPlaceMapper;
import at.backend.tourist.places.DTOs.TouristPlaceDTO;
import at.backend.tourist.places.DTOs.TouristPlaceInsertDTO;
import at.backend.tourist.places.Models.Country;
import at.backend.tourist.places.Models.PlaceCategory;
import at.backend.tourist.places.Models.TouristPlace;
import at.backend.tourist.places.Repository.CountryRepository;
import at.backend.tourist.places.Repository.PlaceCategoryRepository;
import at.backend.tourist.places.Repository.TouristPlaceRepository;
import at.backend.tourist.places.Utils.PlaceRelationships;
import at.backend.tourist.places.Utils.Result;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TouristPlaceServiceImpl implements TouristPlaceService {

    private final TouristPlaceRepository touristPlaceRepository;
    private final CountryRepository countryRepository;
    private final PlaceCategoryRepository placeCategoryRepository;
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
    public Result<PlaceRelationships> validate(TouristPlaceInsertDTO insertDTO) {
        Optional<Country> country = countryRepository.findById(insertDTO.getCountryId());
        Optional<PlaceCategory> placeCategory = placeCategoryRepository.findById(insertDTO.getCategoryId());

        if (country.isEmpty()) {
            return Result.failure("Country not found");
        } else if (placeCategory.isEmpty()) {
            return Result.failure("Place category not found");
        }

        PlaceRelationships placeRelationships =  new PlaceRelationships(country.get(), placeCategory.get());
        return Result.success(placeRelationships);
    }

    @Override
    public TouristPlaceDTO create(TouristPlaceInsertDTO insertDTO) {
        TouristPlace place = touristPlaceMapper.DTOToEntity(insertDTO);

        place.setCountry(insertDTO.getPlaceRelationships().getCountry());
        place.setCategory(insertDTO.getPlaceRelationships().getPlaceCategory());

        touristPlaceRepository.saveAndFlush(place);

        return touristPlaceMapper.entityToDTO(place);
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
