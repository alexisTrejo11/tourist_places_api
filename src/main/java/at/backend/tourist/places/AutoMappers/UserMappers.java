package at.backend.tourist.places.AutoMappers;

import at.backend.tourist.places.DTOs.SignupDTO;
import at.backend.tourist.places.DTOs.TouristPlaceDTO;
import at.backend.tourist.places.DTOs.TouristPlaceInsertDTO;
import at.backend.tourist.places.DTOs.UserDTO;
import at.backend.tourist.places.Models.TouristPlace;
import at.backend.tourist.places.Models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMappers {

    UserDTO entityToDTO(User user);

    @Mapping(target = "id", ignore = true)
    User DTOToEntity(SignupDTO signupDTO);

}