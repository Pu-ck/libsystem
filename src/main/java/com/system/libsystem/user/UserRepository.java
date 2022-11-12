package com.system.libsystem.user;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
public class UserRepository {
    private static final HashMap<String, User> REGISTERED_USERS = new HashMap<>(2);

    @PostConstruct
    public void setUsers() {
        REGISTERED_USERS.put("user1", createUser("user1"
                , "$2a$10$4EvCE3wPMBPYEV/FA8B.3e1mrlCGaVuq.cO0x0fmrt198H61q/dFG"));
        REGISTERED_USERS.put("user2", createUser("user2"
                , "$2a$10$hvOa9FAisXftunkgb/QmkO5FLTQCI123rKTY.yuWAv9DjOW43/cSi"));
    }

    public User createUser(final String username, final String password) {
        final User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        return user;
    }

    public User findUserByUsername(final String username) {
        return REGISTERED_USERS.get(username);
    }

}
