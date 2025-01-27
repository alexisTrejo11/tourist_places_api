package at.backend.tourist.places.modules.Auth.JWT.Token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenFactory {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.accessExpiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refreshExpiration}")
    private long refreshTokenExpiration;

    public TokenGenerator getTokenGenerator(String tokenType) {
        return switch (tokenType.toLowerCase()) {
            case "access" -> new AccessTokenGenerator(secretKey, accessTokenExpiration);
            case "refresh" -> new RefreshTokenGenerator(secretKey, refreshTokenExpiration);
            case "reset" -> new ResetTokenGenerator();
            case "activation" -> new ActivationTokenGenerator();
            default -> throw new IllegalArgumentException("Unknown token type: " + tokenType);
        };
    }
}


