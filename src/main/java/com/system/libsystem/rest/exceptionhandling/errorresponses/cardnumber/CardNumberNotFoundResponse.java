package com.system.libsystem.rest.exceptionhandling.errorresponses.cardnumber;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class CardNumberNotFoundResponse extends ErrorResponse {
    public CardNumberNotFoundResponse() {
        super(404, "Card number not found");
    }
}
