package com.system.libsystem.exceptions.registration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Username already taken")
public class UsernameAlreadyTakenException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Username is already taken";
    }

}
