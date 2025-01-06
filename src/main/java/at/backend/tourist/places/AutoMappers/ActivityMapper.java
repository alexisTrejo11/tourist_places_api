package at.backend.tourist.places.AutoMappers;

import at.backend.tourist.places.DTOs.ActivityDTO;
import at.backend.tourist.places.DTOs.ActivityInsertDTO;
import at.backend.tourist.places.Models.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

    @Mapping(target = "touristPlaceId", source = "touristPlace.id")
    ActivityDTO entityToDTO(Activity activity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "touristPlace", ignore = true)
    Activity DTOToEntity(ActivityInsertDTO insertDTO);

    @Mapping(target = "touristPlace", ignore = true)
    Activity DTOToEntity(ActivityDTO activityDTO);
}
