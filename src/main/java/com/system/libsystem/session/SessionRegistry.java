package com.system.libsystem.session;

import com.system.libsystem.exceptions.session.UnableToGetSessionUsernameException;
import com.system.libsystem.exceptions.session.UnableToRegisterSessionException;
import com.system.libsystem.exceptions.session.UsernameSessionNotProvidedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Component
@Slf4j
public class SessionRegistry {

    private final ValueOperations<String, String> redisSessionStorage;

    @Autowired
    public SessionRegistry(final RedisTemplate<String, String> redisTemplate) {
        redisSessionStorage = redisTemplate.opsForValue();
    }

    public String registerSession(final String username) {
        if (username == null) {
            throw new UsernameSessionNotProvidedException("Username must be provided");
        }

        final String sessionID = getSessionID();

        try {
            redisSessionStorage.set(sessionID, username);
        } catch (final Exception exception) {
            throw new UnableToRegisterSessionException(exception.getMessage());
        }

        log.info("New session with id " + sessionID + " set and saved in Redis repository");

        return sessionID;
    }

    public String getSessionUsername(String sessionID) {
        try {
            return redisSessionStorage.get(sessionID);
        } catch (final Exception exception) {
            throw new UnableToGetSessionUsernameException(exception.getMessage());
        }
    }

    private String getSessionID() {
        return new String(
                Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8))
        );
    }

}
