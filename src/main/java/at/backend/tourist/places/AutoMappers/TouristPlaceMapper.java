package at.backend.tourist.places.AutoMappers;

import at.backend.tourist.places.DTOs.TouristPlaceDTO;
import at.backend.tourist.places.DTOs.TouristPlaceInsertDTO;
import at.backend.tourist.places.Models.TouristPlace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TouristPlaceMapper {
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "countryId", source = "country.id")
    TouristPlaceDTO entityToDTO(TouristPlace review);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    TouristPlace DTOToEntity(TouristPlaceInsertDTO insertDTO);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "country", ignore = true)
    TouristPlace DTOToEntity(TouristPlaceDTO reviewDTO);
}