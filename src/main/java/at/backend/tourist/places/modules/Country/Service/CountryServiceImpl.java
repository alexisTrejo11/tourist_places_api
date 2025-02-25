package at.backend.tourist.places.modules.Country.Service;

import at.backend.tourist.places.core.Exceptions.ResourceAlreadyExistsException;
import at.backend.tourist.places.core.Exceptions.ResourceNotFoundException;
import at.backend.tourist.places.modules.Country.AutoMappers.CountryMapper;
import at.backend.tourist.places.modules.Country.DTOs.CountryDTO;
import at.backend.tourist.places.modules.Country.DTOs.CountryInsertDTO;
import at.backend.tourist.places.core.Utils.Enum.Continent;
import at.backend.tourist.places.modules.Country.Country;
import at.backend.tourist.places.modules.Country.Repository.CountryRepository;
import at.backend.tourist.places.core.Utils.StringHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Override
    @Cacheable(value = "countryById", key = "#id")
    public CountryDTO getById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country", "id", id));

        return countryMapper.entityToDTO(country);
    }

    @Override
    @Cacheable(value = "allCountries")
    public List<CountryDTO> getAll() {
        List<Country> countries = countryRepository.findAll();

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

        String finalName = name;
        Country country = countryRepository.findByName(name)
                .orElseThrow(() -> new ResourceAlreadyExistsException("Country", "name", finalName));

        return countryMapper.entityToDTO(country);
    }

    private void validate(CountryInsertDTO insertDTO) {
        String name = StringHandler.capitalize(insertDTO.getName());

        countryRepository.findByName(name).ifPresent(country -> {
            throw new ResourceAlreadyExistsException("Country", "name", name);
        });
    }

    @Override
    public CountryDTO create(CountryInsertDTO insertDTO) {
        validate(insertDTO);

        Country country = countryMapper.DTOToEntity(insertDTO);
        countryRepository.saveAndFlush(country);

        return countryMapper.entityToDTO(country);
    }

    @Override
    public void delete(Long id) {
        if (!countryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Country", "id", id);
        }

        countryRepository.deleteById(id);
    }
}