package at.backend.tourist.places.Repository;

import at.backend.tourist.places.DTOs.SignupDTO;
import at.backend.tourist.places.Models.User;
import at.backend.tourist.places.Utils.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
