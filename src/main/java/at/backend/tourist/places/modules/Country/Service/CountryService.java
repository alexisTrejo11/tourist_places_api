package at.backend.tourist.places.modules.Country.Service;

import at.backend.tourist.places.modules.Country.DTOs.CountryDTO;
import at.backend.tourist.places.modules.Country.DTOs.CountryInsertDTO;
import at.backend.tourist.places.core.Service.CommonService;
import at.backend.tourist.places.core.Utils.Enum.Continent;
import at.backend.tourist.places.core.Utils.Response.Result;

import java.util.List;

public interface CountryService extends CommonService<CountryDTO, CountryInsertDTO> {
    List<CountryDTO> getByContinent(Continent continent);
    CountryDTO getByName(String name);

    Result<Void> validate(CountryInsertDTO insertDTO);

    }