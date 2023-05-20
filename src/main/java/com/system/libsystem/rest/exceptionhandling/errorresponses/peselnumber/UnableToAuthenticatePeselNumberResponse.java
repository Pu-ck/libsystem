package com.system.libsystem.rest.exceptionhandling.errorresponses.peselnumber;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class UnableToAuthenticatePeselNumberResponse extends ErrorResponse {
    public UnableToAuthenticatePeselNumberResponse() {
        super(401, "PESEL number not authenticated");
    }
}
