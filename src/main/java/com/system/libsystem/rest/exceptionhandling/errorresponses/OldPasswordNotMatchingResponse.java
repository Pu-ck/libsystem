package com.system.libsystem.rest.exceptionhandling.errorresponses;

public class OldPasswordNotMatchingResponse extends ErrorResponse {
    public OldPasswordNotMatchingResponse() {
        super(409, "Old password not matching");
    }
}
