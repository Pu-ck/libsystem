package com.system.libsystem.exceptions.registration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConfirmationTokenNotFoundException extends RuntimeException {

    private final String token;

    @Override
    public String getMessage() {
        return "Unable to find confirmation token " + token;
    }

}
