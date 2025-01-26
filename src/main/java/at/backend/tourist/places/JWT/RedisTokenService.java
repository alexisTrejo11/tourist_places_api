package at.backend.tourist.places.JWT;

import at.backend.tourist.places.Utils.RedisTokenDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisTokenService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    // Guardar un token (válido o blacklisted) con email y status como JSON
    public void saveToken(String token, String email, String status, long ttlSeconds) {
        try {
            RedisTokenDTO redisTokenDTO = new RedisTokenDTO(token, email, status);
            String jsonData = objectMapper.writeValueAsString(redisTokenDTO);
            String keyPrefix = status.equals("blacklisted") ? "blacklisted_token:" : "valid_token:";
            redisTemplate.opsForValue().set(keyPrefix + token, jsonData, ttlSeconds, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing token data", e);
        }
    }

    public RedisTokenDTO getTokenData(String token) {
        try {
            String jsonData = redisTemplate.opsForValue().get("valid_token:" + token);
            if (jsonData == null) {
                jsonData = redisTemplate.opsForValue().get("blacklisted_token:" + token);
            }
            if (jsonData != null) {
                return objectMapper.readValue(jsonData, RedisTokenDTO.class);
            }
            return null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing token data", e);
        }
    }

    public void deleteToken(String token) {
        redisTemplate.delete("valid_token:" + token);
        redisTemplate.delete("blacklisted_token:" + token);
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey("blacklisted_token:" + token);
    }

    public void blacklistToken(String token) {
        saveToken(token, "", "blacklisted", ttlSeconds);
    }

    public boolean validateToken(String token) {
        return redisTemplate.hasKey("valid_token:" + token);
    }
}
