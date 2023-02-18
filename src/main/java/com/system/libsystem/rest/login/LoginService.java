package com.system.libsystem.rest.login;

import com.system.libsystem.session.SessionRegistry;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {

    private AuthenticationManager authenticationManager;
    private SessionRegistry sessionRegistry;

    public ResponseEntity<LoginSessionRequest> login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        final String sessionID = sessionRegistry.registerSession(loginRequest.getUsername());
        LoginSessionRequest loginSessionRequest = new LoginSessionRequest();
        loginSessionRequest.setSessionID(sessionID);

        return ResponseEntity.ok(loginSessionRequest);
    }

}
