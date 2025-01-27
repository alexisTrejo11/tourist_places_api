package at.backend.tourist.places.modules.Places.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for searching tourist places with various criteria, including pagination and sorting options")
public class TouristPlaceSearchDTO {

    @Schema(description = "Name of the tourist place to search for", example = "Eiffel Tower")
    private String name;

    @Schema(description = "Description of the tourist place to search for", example = "A famous landmark in Paris")
    private String description;

    @Schema(description = "Minimum rating of the tourist place to search for", example = "4.5")
    private Double rating;

    @Schema(description = "Name of the country to filter tourist places", example = "France")
    private String countryName;

    @Schema(description = "Name of the category to filter tourist places", example = "Landmarks")
    private String categoryName;

    @Schema(description = "Price range of the tourist place to search for", example = "€€")
    private String priceRange;

    @Schema(description = "Opening hours of the tourist place to search for", example = "09:00-18:00")
    private String openingHours;

    // Pagination and Sorting
    @Schema(description = "Page number for pagination (starting from 0)", example = "0")
    private int page = 0;

    @Schema(description = "Number of items per page for pagination", example = "10")
    private int size = 10;

    @Schema(description = "Field to sort by (default is 'name')", example = "name")
    private String sortBy = "name";

    @Schema(description = "Sorting direction (asc or desc, default is 'asc')", example = "asc")
    private String sortDirection = "asc";
}