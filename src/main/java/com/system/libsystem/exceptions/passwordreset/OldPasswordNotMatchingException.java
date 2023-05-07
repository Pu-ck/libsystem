package com.system.libsystem.exceptions.passwordreset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Old password not matching")
public class OldPasswordNotMatchingException extends RuntimeException {

    @Override
    public String getMessage() {
        return "The old password is not correct";
    }

}
