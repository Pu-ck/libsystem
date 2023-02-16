package com.system.libsystem.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Component
public class SessionRegistry {

    private final ValueOperations<String, String> redisSessionStorage;

    @Autowired
    public SessionRegistry(final RedisTemplate<String, String> redisTemplate) {
        redisSessionStorage = redisTemplate.opsForValue();
    }

    public String registerSession(final String username) {
        if (username == null) {
            throw new IllegalStateException("Username must be provided");
        }

        final String sessionID = getSessionID();

        try {
            redisSessionStorage.set(sessionID, username);
        } catch (final Exception exception) {
            exception.printStackTrace();
        }

        return sessionID;
    }

    public String getSessionUsername(String sessionID) {
        try {
            return redisSessionStorage.get(sessionID);
        } catch (final Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private String getSessionID() {
        return new String(
                Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8))
        );
    }

}
