package com.system.libsystem.rest.registration;

import org.springframework.http.ResponseEntity;

public interface RegistrationService {

    ResponseEntity<RegistrationResponse> register(RegistrationRequest registrationRequest);

    void confirmToken(String token);

}
