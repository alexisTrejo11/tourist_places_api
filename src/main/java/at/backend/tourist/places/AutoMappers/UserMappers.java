package at.backend.tourist.places.AutoMappers;

import at.backend.tourist.places.DTOs.*;
import at.backend.tourist.places.Models.TouristPlace;
import at.backend.tourist.places.Models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMappers {

    UserDTO entityToDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "activated", ignore = true)
    @Mapping(target = "joinedAt", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    User DTOToEntity(SignupDTO signupDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "joinedAt", ignore = true)
    @Mapping(target = "activated", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    User DTOToEntity(UserInsertDTO insertDTO);


}