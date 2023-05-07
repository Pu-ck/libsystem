package com.system.libsystem.exceptions.passwordreminder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Password reminder token expired")
public class PasswordReminderTokenExpiredException extends RuntimeException {

    private final String token;
    private final Long id;

    @Override
    public String getMessage() {
        return "The password reminder token " + token + " with id " + id + " has expired";
    }

}
