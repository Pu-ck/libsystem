package com.system.libsystem.rest.registration.registered;

import org.springframework.http.ResponseEntity;

public interface RegisteredService {

    ResponseEntity<Void> validateRegistrationToken(String token, String username);

}
