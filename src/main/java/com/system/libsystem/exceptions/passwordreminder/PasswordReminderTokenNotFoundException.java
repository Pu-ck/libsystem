package com.system.libsystem.exceptions.passwordreminder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Password reminder token not found")
public class PasswordReminderTokenNotFoundException extends RuntimeException {

    private final String token;

    @Override
    public String getMessage() {
        return "Unable to find password reminder token " + token;
    }


}
