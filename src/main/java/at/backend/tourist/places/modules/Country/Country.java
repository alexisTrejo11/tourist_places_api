package at.backend.tourist.places.modules.Country;

import at.backend.tourist.places.core.Utils.Enum.Continent;
import at.backend.tourist.places.modules.Places.Models.TouristPlace;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
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

    @Column(name = "flag_image")
    private String flagImage;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "country")
    private List<TouristPlace> places;

    public Country(Long id, String name, String capital, String currency, String language,
                   Long population, Double area, Continent continent, String flagImage) {
        this.id = id;
        this.name = name;
        this.capital = capital;
        this.currency = currency;
        this.language = language;
        this.population = population;
        this.area = area;
        this.continent = continent;
        this.flagImage = flagImage;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

