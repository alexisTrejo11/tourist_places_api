package at.backend.tourist.places.AutoMappers;

import at.backend.tourist.places.DTOs.PlaceListDTO;
import at.backend.tourist.places.DTOs.PlaceListInsertDTO;
import at.backend.tourist.places.Models.PlaceList;
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