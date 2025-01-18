package at.backend.tourist.places.Service.Implementation;

import at.backend.tourist.places.AutoMappers.PlaceCategoryMapper;
import at.backend.tourist.places.DTOs.PlaceCategoryDTO;
import at.backend.tourist.places.DTOs.PlaceCategoryInsertDTO;
import at.backend.tourist.places.Models.PlaceCategory;
import at.backend.tourist.places.Repository.PlaceCategoryRepository;
import at.backend.tourist.places.Service.PlaceCategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceCategoryServiceImpl implements PlaceCategoryService {

    private final PlaceCategoryRepository placeCategoryRepository;
    private final PlaceCategoryMapper activityMapper;

    @Override
    public PlaceCategoryDTO create(PlaceCategoryInsertDTO insertDTO) {
        PlaceCategory activity = activityMapper.DTOToEntity(insertDTO);

        placeCategoryRepository.saveAndFlush(activity);

        return activityMapper.entityToDTO(activity);
    }

    @Override
    public PlaceCategoryDTO getById(Long id) {
        Optional<PlaceCategory> optionalPlaceCategory = placeCategoryRepository.findById(id);
        return optionalPlaceCategory
                .map(activityMapper::entityToDTO)
                .orElse(null);

    }

    @Override
    public List<PlaceCategoryDTO> getAll() {
        List<PlaceCategory> placeCategory =  placeCategoryRepository.findAll();

        return placeCategory.stream()
                .map(activityMapper::entityToDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        boolean exists = placeCategoryRepository.existsById(id);
        if (!exists) {
            throw new EntityNotFoundException("PlaceCategory not found");
        }

        placeCategoryRepository.deleteById(id);
    }
}

