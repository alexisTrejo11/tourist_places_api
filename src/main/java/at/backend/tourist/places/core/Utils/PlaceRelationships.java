package at.backend.tourist.places.core.Utils;

import at.backend.tourist.places.modules.Country.Country;
import at.backend.tourist.places.modules.Places.PlaceCategory;
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
