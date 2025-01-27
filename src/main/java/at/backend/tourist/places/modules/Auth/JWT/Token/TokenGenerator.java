package at.backend.tourist.places.modules.Auth.JWT.Token;

public interface TokenGenerator {
    String generateToken(String email, Long userId, String role);
}
