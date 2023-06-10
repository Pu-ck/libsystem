package com.system.libsystem.rest.exceptionhandling.errorresponses.user;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class UserNotFoundResponse extends ErrorResponse {
    public UserNotFoundResponse() {
        super(404, "User not found");
    }
}
