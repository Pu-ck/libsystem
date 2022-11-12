package com.system.libsystem.session;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

@Component
public class SessionRegistry {

    private static final HashMap<String, String> SESSIONS = new HashMap<>();

    public String registerSession(final String username) {
        if (username == null) {
            throw new RuntimeException("Username must be provided");
        }

        final String sessionID = getSessionID();
        SESSIONS.putIfAbsent(sessionID, username);

        return sessionID;
    }

    public String getSessionUsername(String sessionID) {
        return SESSIONS.get(sessionID);
    }

    private String getSessionID() {
        return new String(
                Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8))
        );
    }

}
