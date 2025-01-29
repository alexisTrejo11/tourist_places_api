package at.backend.tourist.places.modules.Places.AutoMappers;

import at.backend.tourist.places.modules.Places.DTOs.PlaceCategoryDTO;
import at.backend.tourist.places.modules.Places.DTOs.PlaceCategoryInsertDTO;
import at.backend.tourist.places.modules.Places.Models.PlaceCategory;
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