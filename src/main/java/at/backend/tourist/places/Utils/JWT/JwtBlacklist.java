package at.backend.tourist.places.Utils.JWT;

import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class JwtBlacklist {
    private final ConcurrentHashMap<String, Boolean> blacklist = new ConcurrentHashMap<>();

    public void invalidateToken(String token) {
        blacklist.put(token, true);
    }

    public boolean isTokenInvalidated(String token) {
        return blacklist.containsKey(token);
    }
}

