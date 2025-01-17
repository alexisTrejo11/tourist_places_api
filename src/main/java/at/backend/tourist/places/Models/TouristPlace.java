package at.backend.tourist.places.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tourist_places")
public class TouristPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "rating", nullable = false)
    private Double rating = 0.0;

    @Column(name = "image")
    private String image;

    @Column(name = "opening_hours")
    private String openingHours;

    @Column(name = "price_range")
    private String priceRange;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private PlaceCategory category;

    @OneToMany(mappedBy = "touristPlace", fetch = FetchType.LAZY)
    private List<Activity> activities;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY)
    private List<Review> reviews;
}