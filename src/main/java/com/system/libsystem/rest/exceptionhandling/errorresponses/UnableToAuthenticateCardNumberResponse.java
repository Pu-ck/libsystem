package com.system.libsystem.rest.exceptionhandling.errorresponses;

public class UnableToAuthenticateCardNumberResponse extends ErrorResponse {
    public UnableToAuthenticateCardNumberResponse() {
        super(400, "Card number not authenticated");
    }
}
