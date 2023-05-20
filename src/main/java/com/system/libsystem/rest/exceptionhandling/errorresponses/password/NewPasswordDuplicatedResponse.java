package com.system.libsystem.rest.exceptionhandling.errorresponses.password;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class NewPasswordDuplicatedResponse extends ErrorResponse {
    public NewPasswordDuplicatedResponse() {
        super(409, "New password same as old one");
    }
}
