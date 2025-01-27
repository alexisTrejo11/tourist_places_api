package at.backend.tourist.places.JWT.Token;

public interface TokenGenerator {
    String generateToken(String email, Long userId, String role);
}
