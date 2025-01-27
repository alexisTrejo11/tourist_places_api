package at.backend.tourist.places.modules.Places.AutoMappers;

import at.backend.tourist.places.modules.Places.DTOs.PlaceListDTO;
import at.backend.tourist.places.modules.Places.DTOs.PlaceListInsertDTO;
import at.backend.tourist.places.modules.Places.PlaceList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaceListMapper {

    @Mapping(target = "userId", source = "user.id")
    PlaceListDTO entityToDTO(PlaceList placeList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "places", ignore = true)
    PlaceList DTOToEntity(PlaceListInsertDTO insertDTO);

    @Mapping(target = "places", ignore = true)
    PlaceList DTOToEntity(PlaceListDTO activityDTO);
}