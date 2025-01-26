package at.backend.tourist.places.JWT.Token;

import java.security.SecureRandom;

public class ResetTokenGenerator implements TokenGenerator {
    private static final String CHARACTERS = "0123456789";
    private static final int TOKEN_LENGTH = 6;


    @Override
    public String generateToken(String email, String role) {
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            token.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return token.toString();
    }
}

