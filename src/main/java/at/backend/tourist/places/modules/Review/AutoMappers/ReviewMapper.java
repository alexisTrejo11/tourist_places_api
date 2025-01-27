package at.backend.tourist.places.modules.Review.AutoMappers;

import at.backend.tourist.places.modules.Review.DTOs.ReviewDTO;
import at.backend.tourist.places.modules.Review.DTOs.ReviewInsertDTO;
import at.backend.tourist.places.modules.Review.DTOs.ReviewUpdateDTO;
import at.backend.tourist.places.modules.Review.Review;
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