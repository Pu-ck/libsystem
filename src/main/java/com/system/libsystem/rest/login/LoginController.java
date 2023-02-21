package com.system.libsystem.rest.login;

import com.system.libsystem.rest.passwordreminder.PasswordReminderRequest;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home/login")
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<LoginSessionRequest> login(@RequestBody LoginRequest loginRequest) {
        return loginService.login(loginRequest);
    }

}
