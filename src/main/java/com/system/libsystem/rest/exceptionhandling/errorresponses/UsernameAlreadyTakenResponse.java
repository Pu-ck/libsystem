package com.system.libsystem.rest.exceptionhandling.errorresponses;

public class UsernameAlreadyTakenResponse extends ErrorResponse {
    public UsernameAlreadyTakenResponse() {
        super(409, "Username already taken");
    }
}
