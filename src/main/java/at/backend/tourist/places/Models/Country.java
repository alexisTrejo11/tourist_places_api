package at.backend.tourist.places.Models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "countries")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "capital", nullable = false)
    private String capital;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "population", nullable = false)
    private Long population;

    @Column(name = "area", nullable = false)
    private Double area;

    @Column(name = "continent", nullable = false)
    @Enumerated(EnumType.STRING)
    private Continent continent;

    @Column(name = "flag_image", nullable = true)
    private String flagImage;

    @OneToMany(mappedBy = "country")
    private List<TouristPlace> places;
}

