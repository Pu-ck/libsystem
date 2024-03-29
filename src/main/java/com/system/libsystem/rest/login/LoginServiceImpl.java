package com.system.libsystem.rest.login;

import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.entities.user.UserServiceImpl;
import com.system.libsystem.session.SessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final SessionRegistry sessionRegistry;
    private final UserService userService;

    @Override
    public ResponseEntity<LoginSessionResponse> login(LoginRequest loginRequest) {
        final UserEntity userEntity = userService.loadUserByUsername(loginRequest.getUsername());
        final LoginSessionResponse loginSessionResponse = new LoginSessionResponse();
        UserServiceImpl.validateIfUserIsEnabled(userEntity);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        final String sessionID = sessionRegistry.registerSession(loginRequest.getUsername());

        loginSessionResponse.setSessionID(sessionID);
        loginSessionResponse.setUserType(userEntity.getUserType());
        log.info("New user with id " + userEntity.getId() + " has logged in");

        return ResponseEntity.ok(loginSessionResponse);
    }

}
