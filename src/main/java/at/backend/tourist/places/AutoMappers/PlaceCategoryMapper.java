package at.backend.tourist.places.AutoMappers;

import at.backend.tourist.places.DTOs.ActivityDTO;
import at.backend.tourist.places.DTOs.PlaceCategoryDTO;
import at.backend.tourist.places.DTOs.PlaceCategoryInsertDTO;
import at.backend.tourist.places.Models.PlaceCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaceCategoryMapper {
    PlaceCategoryDTO entityToDTO(PlaceCategory placeCategory);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "places", ignore = true)
    PlaceCategory DTOToEntity(PlaceCategoryInsertDTO insertDTO);

    @Mapping(target = "places", ignore = true)
    PlaceCategory DTOToEntity(PlaceCategoryDTO activityDTO);
}