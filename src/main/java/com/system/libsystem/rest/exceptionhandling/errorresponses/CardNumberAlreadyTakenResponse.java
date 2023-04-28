package com.system.libsystem.rest.exceptionhandling.errorresponses;

public class CardNumberAlreadyTakenResponse extends ErrorResponse {
    public CardNumberAlreadyTakenResponse() {
        super(409, "Card number already taken");
    }
}
