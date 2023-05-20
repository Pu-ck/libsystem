package com.system.libsystem.rest.exceptionhandling.errorresponses.cardnumber;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class UnableToAuthenticateCardNumberResponse extends ErrorResponse {
    public UnableToAuthenticateCardNumberResponse() {
        super(400, "Card number not authenticated");
    }
}
