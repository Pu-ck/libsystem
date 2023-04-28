package com.system.libsystem.rest.login;

import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.session.SessionRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final SessionRegistry sessionRegistry;
    private final UserService userService;

    public ResponseEntity<LoginSessionResponse> login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));

        final UserEntity userEntity = userService.loadUserByUsername(loginRequest.getUsername());
        final String sessionID = sessionRegistry.registerSession(loginRequest.getUsername());

        LoginSessionResponse loginSessionResponse = new LoginSessionResponse();
        loginSessionResponse.setSessionID(sessionID);
        log.info("New user with id " + userEntity.getId() + " has logged in");

        return ResponseEntity.ok(loginSessionResponse);
    }

}
