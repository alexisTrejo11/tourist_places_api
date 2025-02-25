package at.backend.tourist.places.modules.Places.Service;

import at.backend.tourist.places.core.Exceptions.BusinessLogicException;
import at.backend.tourist.places.core.Exceptions.ResourceNotFoundException;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
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
        TouristPlace touristPlace = touristPlaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tourist Place", "id", id));

        return touristPlaceMapper.entityToDTO(touristPlace);
    }

    @Override
    public List<TouristPlaceDTO> getAll() {
        List<TouristPlace> activities = touristPlaceRepository.findAll();

        return activities.stream()
                .map(touristPlaceMapper::entityToDTO)
                .toList();
    }

    @Override
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
        if (idsList == null || idsList.isEmpty()) {
            throw new BusinessLogicException("ID list cannot be empty");
        }

        Set<TouristPlace> touristPlaces = touristPlaceRepository.findByIdIn(idsList);

        Set<Long> foundIds = touristPlaces.stream()
                .map(TouristPlace::getId)
                .collect(Collectors.toSet());

        List<Long> missingIds = idsList.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new ResourceNotFoundException("Tourist Places not found with IDs: " + missingIds);
        }

        return touristPlaces.stream()
                .map(touristPlaceMapper::entityToDTO)
                .toList();
    }

    @Override
    public List<TouristPlaceDTO> getByCountry(Long countryId) {
        if (!countryRepository.existsById(countryId)) {
            throw new ResourceNotFoundException("Country", "id", countryId);
        }

        List<TouristPlace> places = touristPlaceRepository.findByCountryId(countryId);

        return places.stream()
                .map(touristPlaceMapper::entityToDTO)
                .toList();
    }

    @Override
    public List<TouristPlaceDTO> getByCategory(Long categoryId) {
        if (!placeCategoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Place Category", "id", categoryId);
        }

        List<TouristPlace> places = touristPlaceRepository.findByCategoryId(categoryId);

        return places.stream()
                .map(touristPlaceMapper::entityToDTO)
                .toList();
    }

    private PlaceRelationships validate(TouristPlaceInsertDTO insertDTO) {
        if (insertDTO == null) {
            throw new BusinessLogicException("Tourist place data cannot be null");
        }

        if (insertDTO.getName() == null || insertDTO.getName().trim().isEmpty()) {
            throw new BusinessLogicException("Tourist place name cannot be empty");
        }

        Country country = countryRepository.findById(insertDTO.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException("Country", "id", insertDTO.getCountryId()));

        PlaceCategory placeCategory = placeCategoryRepository.findById(insertDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Place Category", "id", insertDTO.getCategoryId()));

        return new PlaceRelationships(country, placeCategory);
    }

    @Override
    public TouristPlaceDTO create(TouristPlaceInsertDTO insertDTO) {
        log.info("Starting to create tourist place with name: {}", insertDTO.getName());

        PlaceRelationships relationships = validate(insertDTO);
        insertDTO.setPlaceRelationships(relationships);

        TouristPlace place = touristPlaceMapper.DTOToEntity(insertDTO);
        place.setCountry(relationships.getCountry());
        place.setCategory(relationships.getPlaceCategory());

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
                    return new ResourceNotFoundException("Tourist Place", "id", id);
                });

        double averageRating = calculateRatingAverage(touristPlace);
        touristPlace.setRating(averageRating);

        touristPlaceRepository.save(touristPlace);

        log.info("Rating updated for tourist place with ID: {}. New average rating: {}", id, averageRating);
    }

    @Override
    public void delete(Long id) {
        log.info("Attempting to delete tourist place with ID: {}", id);

        if (!touristPlaceRepository.existsById(id)) {
            log.error("Tourist place with ID: {} not found for deletion", id);
            throw new ResourceNotFoundException("Tourist Place", "id", id);
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

        // Round to one decimal place
        averageRating = Math.round(averageRating * 10.0) / 10.0;

        log.debug("Calculated average rating for tourist place: {}", averageRating);

        return averageRating;
    }
}