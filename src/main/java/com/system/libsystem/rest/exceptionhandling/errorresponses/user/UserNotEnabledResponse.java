package com.system.libsystem.rest.exceptionhandling.errorresponses.user;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class UserNotEnabledResponse extends ErrorResponse {
    public UserNotEnabledResponse() {
        super(403, "User not enabled");
    }
}
