package com.system.libsystem.rest.exceptionhandling.errorresponses.cardnumber;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class CardNumberAlreadyTakenResponse extends ErrorResponse {
    public CardNumberAlreadyTakenResponse() {
        super(409, "Card number already taken");
    }
}
