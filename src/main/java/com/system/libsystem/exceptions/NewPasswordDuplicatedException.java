package com.system.libsystem.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "New password same as old one")
public class NewPasswordDuplicatedException extends RuntimeException {

    @Override
    public String getMessage() {
        return "The new password is the same as old one";
    }

}
