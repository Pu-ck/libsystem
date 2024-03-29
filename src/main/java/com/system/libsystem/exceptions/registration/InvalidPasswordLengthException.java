package com.system.libsystem.exceptions.registration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidPasswordLengthException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Invalid password length";
    }

}
