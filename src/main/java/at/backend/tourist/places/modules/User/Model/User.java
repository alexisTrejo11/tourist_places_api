package at.backend.tourist.places.modules.User.Model;

import at.backend.tourist.places.modules.Places.Models.PlaceList;
import at.backend.tourist.places.modules.Review.Review;
import at.backend.tourist.places.core.Utils.Enum.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    private String password;

    private String provider;

    private boolean isActivated;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Enumerated(EnumType.STRING)
    private Role role = Role.VIEWER;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @PrePersist
    private void onCreate() {
        this.joinedAt = LocalDateTime.now();
        this.lastLogin = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PlaceList> placeLists;
}
