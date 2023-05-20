package com.system.libsystem.rest.exceptionhandling.errorresponses.password;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class OldPasswordNotMatchingResponse extends ErrorResponse {
    public OldPasswordNotMatchingResponse() {
        super(409, "Old password not matching");
    }
}
