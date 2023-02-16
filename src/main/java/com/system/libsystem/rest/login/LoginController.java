package com.system.libsystem.rest.login;

import com.system.libsystem.session.SessionRegistry;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@AllArgsConstructor
public class LoginController {

    private AuthenticationManager authenticationManager;
    private SessionRegistry sessionRegistry;

    @PostMapping
    public ResponseEntity<LoginSessionRequest> login(@RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        final String sessionID = sessionRegistry.registerSession(loginRequest.getUsername());
        LoginSessionRequest loginSessionRequest = new LoginSessionRequest();
        loginSessionRequest.setSessionID(sessionID);

        return ResponseEntity.ok(loginSessionRequest);
    }
}
