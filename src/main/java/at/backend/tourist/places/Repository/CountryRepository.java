package at.backend.tourist.places.Repository;

import at.backend.tourist.places.Models.TouristPlace;
import at.backend.tourist.places.Utils.Enum.Continent;
import at.backend.tourist.places.Models.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    List<Country> findByContinent(Continent continent);
    Optional<Country> findByName(String name);

    Page<TouristPlace> findAll(Specification<TouristPlace> touristPlaceSpecification, Pageable pageable);
}
