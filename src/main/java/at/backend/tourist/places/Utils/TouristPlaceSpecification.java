package at.backend.tourist.places.Utils;

import at.backend.tourist.places.Models.TouristPlace;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TouristPlaceSpecification {

    public static Specification<TouristPlace> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<TouristPlace> hasDescription(String description) {
        return (root, query, criteriaBuilder) -> {
            if (description == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
        };
    }

    public static Specification<TouristPlace> hasRating(Double rating) {
        return (root, query, criteriaBuilder) -> {
            if (rating == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), rating);
        };
    }

    public static Specification<TouristPlace> hasCountry(String countryName) {
        return (root, query, criteriaBuilder) -> {
            if (countryName == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("country").get("name")), "%" + countryName.toLowerCase() + "%");
        };
    }

    public static Specification<TouristPlace> hasCategory(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (categoryName == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("category").get("name")), "%" + categoryName.toLowerCase() + "%");
        };
    }

    public static Specification<TouristPlace> hasPriceRange(String priceRange) {
        return (root, query, criteriaBuilder) -> {
            if (priceRange == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("priceRange"), priceRange);
        };
    }

    public static Specification<TouristPlace> hasOpeningHours(String openingHours) {
        return (root, query, criteriaBuilder) -> {
            if (openingHours == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("openingHours"), openingHours);
        };
    }

    @SafeVarargs
    public static Specification<TouristPlace> combineSpecifications(Specification<TouristPlace>... specifications) {
        Specification<TouristPlace> combinedSpec = Specification.where(null);
        for (Specification<TouristPlace> spec : specifications) {
            if (spec != null) {
                combinedSpec = combinedSpec.and(spec);
            }
        }
        return combinedSpec;
    }
}