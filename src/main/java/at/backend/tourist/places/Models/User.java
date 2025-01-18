package at.backend.tourist.places.Models;

import at.backend.tourist.places.Utils.Enum.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
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

    @PrePersist
    private void onCreate() {
        this.joinedAt = LocalDateTime.now();
        this.lastLogin = LocalDateTime.now();
    }
}
