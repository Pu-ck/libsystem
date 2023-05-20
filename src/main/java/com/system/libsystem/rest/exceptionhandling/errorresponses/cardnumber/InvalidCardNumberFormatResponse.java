package com.system.libsystem.rest.exceptionhandling.errorresponses.cardnumber;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class InvalidCardNumberFormatResponse extends ErrorResponse {
    public InvalidCardNumberFormatResponse() {
        super(400, "Invalid card number format");
    }
}
