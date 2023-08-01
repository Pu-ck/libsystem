package com.system.libsystem.rest.registration.registered;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/registered")
@AllArgsConstructor
public class RegisteredController {

    private final RegisteredService registeredService;

    @GetMapping()
    public ResponseEntity<Void> validateRegistrationToken(@RequestParam("token") String token,
                                                          @RequestParam("username") String username) {
        return registeredService.validateRegistrationToken(token, username);
    }

}
