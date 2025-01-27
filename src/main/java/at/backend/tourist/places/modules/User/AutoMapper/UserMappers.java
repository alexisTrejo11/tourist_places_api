package at.backend.tourist.places.modules.User.AutoMapper;

import at.backend.tourist.places.modules.Auth.DTOs.SignupDTO;
import at.backend.tourist.places.modules.User.DTOs.UserDTO;
import at.backend.tourist.places.modules.User.DTOs.UserInsertDTO;
import at.backend.tourist.places.modules.User.Model.User;
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