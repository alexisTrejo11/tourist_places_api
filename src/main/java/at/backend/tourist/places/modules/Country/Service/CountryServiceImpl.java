package at.backend.tourist.places.modules.Country.Service;

import at.backend.tourist.places.modules.Country.AutoMappers.CountryMapper;
import at.backend.tourist.places.modules.Country.DTOs.CountryDTO;
import at.backend.tourist.places.modules.Country.DTOs.CountryInsertDTO;
import at.backend.tourist.places.core.Utils.Enum.Continent;
import at.backend.tourist.places.modules.Country.Country;
import at.backend.tourist.places.modules.Country.Repository.CountryRepository;
import at.backend.tourist.places.core.Utils.Result;
import at.backend.tourist.places.core.Utils.StringHandler;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Override
    @Cacheable(value = "countryById", key = "#id")
    public CountryDTO getById(Long id) {
        Optional<Country> optionalCountry = countryRepository.findById(id);
        return optionalCountry
                .map(countryMapper::entityToDTO)
                .orElse(null);
    }

    @Override
    @Cacheable(value = "allCountries")
    public List<CountryDTO> getAll() {
        List<Country> countries =  countryRepository.findAll();

        return countries.stream()
                .map(countryMapper::entityToDTO)
                .toList();
    }

    @Override
    @Cacheable(value = "countriesByContinent", key = "#continent.name")
    public List<CountryDTO> getByContinent(Continent continent) {
        List<Country> countries = countryRepository.findByContinent(continent);

        return countries.stream()
                .map(countryMapper::entityToDTO)
                .toList();
    }

    @Override
    @Cacheable(value = "countryByName", key = "#name")
    public CountryDTO getByName(String name) {
        name = StringHandler.capitalize(name);

        Optional<Country> optionalCountry = countryRepository.findByName(name);
        return optionalCountry
                .map(countryMapper::entityToDTO)
                .orElse(null);
    }


    public Result<Void> validate(CountryInsertDTO insertDTO) {
        String name = StringHandler.capitalize(insertDTO.getName());

        Optional<Country> optionalCountry = countryRepository.findByName(name);
        if (optionalCountry.isPresent()) {
            return Result.failure("Country already created");
        }

        return Result.success(null);
    }

    @Override
    public CountryDTO create(CountryInsertDTO insertDTO) {
        Country country = countryMapper.DTOToEntity(insertDTO);

        countryRepository.saveAndFlush(country);

        return countryMapper.entityToDTO(country);
    }

    @Override
    public void delete(Long id) {
        boolean exists = countryRepository.existsById(id);
        if (!exists) {
            throw new EntityNotFoundException("Activity not found");
        }

        countryRepository.deleteById(id);
    }
}
