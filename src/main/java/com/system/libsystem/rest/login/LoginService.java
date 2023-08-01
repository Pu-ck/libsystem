package com.system.libsystem.rest.login;

import org.springframework.http.ResponseEntity;

public interface LoginService {

    ResponseEntity<LoginSessionResponse> login(LoginRequest loginRequest);

}
