package com.system.libsystem.rest.exceptionhandling.errorresponses;

public class NewPasswordDuplicatedResponse extends ErrorResponse {
    public NewPasswordDuplicatedResponse() {
        super(409, "New password same as old one");
    }
}
