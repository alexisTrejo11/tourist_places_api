package at.backend.tourist.places.Utils;

import at.backend.tourist.places.Models.Country;
import at.backend.tourist.places.Models.PlaceCategory;
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
