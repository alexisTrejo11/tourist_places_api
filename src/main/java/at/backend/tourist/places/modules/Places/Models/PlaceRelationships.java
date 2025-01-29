package at.backend.tourist.places.modules.Places.Models;

import at.backend.tourist.places.modules.Country.Country;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceRelationships {
    public Country country;
    public PlaceCategory placeCategory;

}
