package at.backend.tourist.places.JWT;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenService {

    private static final String BLACKLISTED_PREFIX = "blacklisted_token:";
    private static final String VALIDATION_PREFIX = "validation_token:";

    private final StringRedisTemplate redisTemplate;

    public RedisTokenService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveToken(String token, String email, String type, long ttlSeconds) {
        if (token == null || token.isEmpty() || email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Token and email must not be null or empty");
        }

        String keyPrefix = type.equals("blacklisted") ? BLACKLISTED_PREFIX : VALIDATION_PREFIX;
        redisTemplate.opsForValue().set(keyPrefix + token, email, ttlSeconds, TimeUnit.SECONDS);
    }

    public String getTokenData(String token) {
        for (String prefix : List.of(VALIDATION_PREFIX, BLACKLISTED_PREFIX)) {
            String data = redisTemplate.opsForValue().get(prefix + token);
            if (data != null) {
                return data;
            }
        }
        return null;
    }

    public void deleteToken(String token) {
        redisTemplate.delete(VALIDATION_PREFIX + token);
        redisTemplate.delete(BLACKLISTED_PREFIX + token);
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey(BLACKLISTED_PREFIX + token);
    }

    public void blacklistToken(String token) {
        redisTemplate.opsForValue().set(BLACKLISTED_PREFIX + token, "", 10800, TimeUnit.SECONDS);
    }

    public boolean validateToken(String token) {
        return redisTemplate.hasKey(VALIDATION_PREFIX + token);
    }
}
