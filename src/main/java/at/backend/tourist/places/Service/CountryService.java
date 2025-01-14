package at.backend.tourist.places.Service;

import at.backend.tourist.places.DTOs.CountryDTO;
import at.backend.tourist.places.DTOs.CountryInsertDTO;
import at.backend.tourist.places.Utils.Enum.Continent;
import at.backend.tourist.places.Utils.Result;

import java.util.List;

public interface CountryService extends CommonService<CountryDTO, CountryInsertDTO> {
    List<CountryDTO> getByContinent(Continent continent);
    CountryDTO getByName(String name);

    Result<Void> validate(CountryInsertDTO insertDTO);

    }