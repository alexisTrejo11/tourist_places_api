package at.backend.tourist.places.modules.Places.Service;

import at.backend.tourist.places.modules.Places.AutoMappers.TouristPlaceMapper;
import at.backend.tourist.places.modules.Places.DTOs.TouristPlaceDTO;
import at.backend.tourist.places.modules.Places.DTOs.TouristPlaceInsertDTO;
import at.backend.tourist.places.modules.Places.DTOs.TouristPlaceSearchDTO;
import at.backend.tourist.places.modules.Country.Country;
import at.backend.tourist.places.modules.Places.Models.PlaceCategory;
import at.backend.tourist.places.modules.Places.Repository.PlaceCategoryRepository;
import at.backend.tourist.places.modules.Places.Repository.TouristPlaceRepository;
import at.backend.tourist.places.modules.Review.Review;
import at.backend.tourist.places.modules.Places.Models.TouristPlace;
import at.backend.tourist.places.modules.Country.Repository.CountryRepository;
import at.backend.tourist.places.modules.Places.Models.PlaceRelationships;
import at.backend.tourist.places.core.Utils.Response.Result;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TouristPlaceServiceImpl implements TouristPlaceService {

    private final TouristPlaceRepository touristPlaceRepository;
    private final CountryRepository countryRepository;
    private final PlaceCategoryRepository placeCategoryRepository;
    private final TouristPlaceMapper touristPlaceMapper;
    private final TouristPlaceSpecification specification;

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

    public Page<TouristPlaceDTO> searchTouristPlaces(TouristPlaceSearchDTO searchDTO, Pageable pageable) {
        Specification<TouristPlace> spec = TouristPlaceSpecification.combineSpecifications(
                TouristPlaceSpecification.hasName(searchDTO.getName()),
                TouristPlaceSpecification.hasDescription(searchDTO.getDescription()),
                TouristPlaceSpecification.hasRating(searchDTO.getRating()),
                TouristPlaceSpecification.hasCountry(searchDTO.getCountryName()),
                TouristPlaceSpecification.hasCategory(searchDTO.getCategoryName()),
                TouristPlaceSpecification.hasPriceRange(searchDTO.getPriceRange()),
                TouristPlaceSpecification.hasOpeningHours(searchDTO.getOpeningHours())
        );

        Page<TouristPlace> touristPlacePage = touristPlaceRepository.findAll(spec, pageable);

        return touristPlacePage.map(touristPlaceMapper::entityToDTO);
    }

    @Override
    public List<TouristPlaceDTO> getByIdList(Set<Long> idsList) {
        Set<TouristPlace> touristPlaces = touristPlaceRepository.findByIdIn(idsList);

        Set<Long> foundIds = touristPlaces.stream()
                .map(TouristPlace::getId)
                .collect(Collectors.toSet());

        List<Long> missingIds = idsList.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new EntityNotFoundException("Not found IDs for places: " + missingIds);
        }

        return touristPlaces.stream()
                .map(touristPlaceMapper::entityToDTO)
                .toList();
    }



    @Override
    public List<TouristPlaceDTO> getByCountry(Long countryId) {
        boolean isCountryExisting = countryRepository.existsById(countryId);
        if (!isCountryExisting) {
            return null;
        }

        List<TouristPlace> places = touristPlaceRepository.findByCountryId(countryId);

        return places.stream()
                .map(touristPlaceMapper::entityToDTO)
                .toList();
    }

    @Override
    public List<TouristPlaceDTO> getByCategory(Long categoryId) {
        boolean isCategoryExisting = placeCategoryRepository.existsById(categoryId);
        if (!isCategoryExisting) {
            return null;
        }

        List<TouristPlace> places = touristPlaceRepository.findByCategoryId(categoryId);

        return places.stream()
                .map(touristPlaceMapper::entityToDTO)
                .toList();
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
        log.info("Starting to create tourist place with name: {}", insertDTO.getName());

        TouristPlace place = touristPlaceMapper.DTOToEntity(insertDTO);
        place.setCountry(insertDTO.getPlaceRelationships().getCountry());
        place.setCategory(insertDTO.getPlaceRelationships().getPlaceCategory());

        touristPlaceRepository.saveAndFlush(place);

        log.info("Tourist place created successfully with ID: {}", place.getId());

        return touristPlaceMapper.entityToDTO(place);
    }

    @Override
    @Async("taskExecutor")
    public void updatePlaceRating(Long id) {
        log.info("Starting to update rating for tourist place with ID: {}", id);

        TouristPlace touristPlace = touristPlaceRepository.findByIdWithReviews(id)
                .orElseThrow(() -> {
                    log.error("Tourist place with ID: {} not found for rating update", id);
                    return new EntityNotFoundException("Tourist place not found");
                });

        double averageRating = calculateRatingAverage(touristPlace);
        touristPlace.setRating(averageRating);

        touristPlaceRepository.save(touristPlace);

        log.info("Rating updated for tourist place with ID: {}. New average rating: {}", id, averageRating);
    }

    @Override
    public void delete(Long id) {
        log.info("Attempting to delete tourist place with ID: {}", id);

        boolean exists = touristPlaceRepository.existsById(id);
        if (!exists) {
            log.error("Tourist place with ID: {} not found for deletion", id);
            throw new EntityNotFoundException("TouristPlace not found");
        }

        touristPlaceRepository.deleteById(id);
        log.info("Tourist place with ID: {} deleted successfully", id);
    }

    private double calculateRatingAverage(TouristPlace touristPlace) {
        double averageRating = touristPlace.getReviews().stream()
                .map(Review::getRating)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        // Round in one decimal
        averageRating = Math.round(averageRating * 10.0) / 10.0;

        log.debug("Calculated average rating for tourist place: {}", averageRating);

        return averageRating;
    }
}
