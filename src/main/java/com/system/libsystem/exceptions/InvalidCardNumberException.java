package com.system.libsystem.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidCardNumberException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Invalid card number";
    }

}
