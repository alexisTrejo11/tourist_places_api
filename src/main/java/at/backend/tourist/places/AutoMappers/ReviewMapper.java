package at.backend.tourist.places.AutoMappers;

import at.backend.tourist.places.DTOs.ReviewDTO;
import at.backend.tourist.places.DTOs.ReviewInsertDTO;
import at.backend.tourist.places.DTOs.ReviewUpdateDTO;
import at.backend.tourist.places.Models.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "placeId", source = "place.id")
    @Mapping(target = "authorId", source = "author.id")
    ReviewDTO entityToDTO(Review review);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "place", ignore = true)
    Review DTOToEntity(ReviewInsertDTO insertDTO);

    @Mapping(target = "place", ignore = true)
    Review DTOToEntity(ReviewDTO reviewDTO);

    void update(@MappingTarget Review review, ReviewUpdateDTO updateDTO);
}