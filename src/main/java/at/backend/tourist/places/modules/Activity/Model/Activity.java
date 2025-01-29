package at.backend.tourist.places.modules.Activity.Model;

import at.backend.tourist.places.modules.Places.Models.TouristPlace;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "duration", nullable = false)
    private String duration;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id", nullable = false)
    private TouristPlace touristPlace;
}