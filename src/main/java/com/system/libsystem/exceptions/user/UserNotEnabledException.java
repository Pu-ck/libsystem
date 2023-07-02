package com.system.libsystem.exceptions.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User not enabled")
public class UserNotEnabledException extends RuntimeException {

    private final Long userId;

    @Override
    public String getMessage() {
        return "User with id " + userId + " is not enabled";
    }

}
