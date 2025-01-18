package at.backend.tourist.places.Utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHandler {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    public static boolean validatePassword(String plainPassword, String hashedPassword) {
        // For oauth2 users that doesn't have password
        if (hashedPassword == null) {
            return false;
        }

        return passwordEncoder.matches(plainPassword, hashedPassword);
    }
}
