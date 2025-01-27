package at.backend.tourist.places.JWT;

import at.backend.tourist.places.DTOs.LoginResponseDTO;
import at.backend.tourist.places.JWT.Token.TokenFactory;
import at.backend.tourist.places.JWT.Token.TokenGenerator;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private final TokenFactory tokenFactory;

    private final RedisTokenService redisTokenService;

    public String generateAccessToken(String email, Long userId, String role) {
        TokenGenerator tokenGenerator = tokenFactory.getTokenGenerator("access");
        return tokenGenerator.generateToken(email, userId, role);
    }

    public String generateRefreshToken(String email, Long userId, String role) {
        TokenGenerator tokenGenerator = tokenFactory.getTokenGenerator("refresh");
        return tokenGenerator.generateToken(email, userId, role);
    }

    public String generateResetToken(String email) {
        TokenGenerator tokenGenerator = tokenFactory.getTokenGenerator("reset");
        return tokenGenerator.generateToken(email, 0L, "");
    }

    public String generateActivateToken(String email) {
        TokenGenerator tokenGenerator = tokenFactory.getTokenGenerator("activation");
        return tokenGenerator.generateToken(email, 0L, "");
    }

    public LoginResponseDTO generateLoginTokens(String email, Long userId ,String role) {
        String accessToken = tokenFactory.getTokenGenerator("access").generateToken(email, userId, role);
        String refreshToken = tokenFactory.getTokenGenerator("refresh").generateToken(email, userId, role);

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        if (token.length() == 6) {
            return validateCustomerToken(token);

        } else  {
            return validateJWTToken(token);
        }
    }

    private boolean validateJWTToken(String token) {
        try {
            if (redisTokenService.isTokenBlacklisted(token)) {
                return false;
            }

            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private boolean validateCustomerToken(String token) {
        if (redisTokenService.isTokenBlacklisted(token)) {
            return false;
        }

        return redisTokenService.validateToken(token);
    }

    public String getEmailFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header.");
        }

        String token = authHeader.substring(7).trim();
        return getEmailFromToken(token);
    }

    public Long getIdFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header.");
        }

        String token = authHeader.substring(7).trim();
        return getIdFromToken(token);
    }

    public void deleteToken(String token) {
        redisTokenService.deleteToken(token);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Long getIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Long.class);
    }
}