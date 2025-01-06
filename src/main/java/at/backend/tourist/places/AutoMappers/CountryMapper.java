package at.backend.tourist.places.AutoMappers;

import at.backend.tourist.places.DTOs.CountryDTO;
import at.backend.tourist.places.DTOs.CountryInsertDTO;
import at.backend.tourist.places.Models.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    CountryDTO entityToDTO(Country country);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "places", ignore = true)
    Country DTOToEntity(CountryInsertDTO insertDTO);
    
    @Mapping(target = "places", ignore = true)
    Country DTOToEntity(CountryDTO countryDTO);
}