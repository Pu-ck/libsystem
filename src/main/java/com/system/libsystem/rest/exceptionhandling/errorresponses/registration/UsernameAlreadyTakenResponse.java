package com.system.libsystem.rest.exceptionhandling.errorresponses.user;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class UsernameAlreadyTakenResponse extends ErrorResponse {
    public UsernameAlreadyTakenResponse() {
        super(409, "Username already taken");
    }
}
