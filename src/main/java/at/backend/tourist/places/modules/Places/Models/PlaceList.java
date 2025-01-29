package at.backend.tourist.places.modules.Places.Models;

import at.backend.tourist.places.modules.User.Model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tourist_place_lists")
public class PlaceList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "place_list_tourist_places",
            joinColumns = @JoinColumn(name = "place_list_id"),
            inverseJoinColumns = @JoinColumn(name = "tourist_place_id")
    )
    private Set<TouristPlace> places;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}
