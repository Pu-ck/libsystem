package com.system.libsystem.exceptions.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not found")
public class UserNotFoundException extends RuntimeException {

    private final Long userId;

    @Override
    public String getMessage() {
        return "Unable to find book with id " + userId;
    }

}
