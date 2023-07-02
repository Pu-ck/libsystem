package com.system.libsystem.rest.exceptionhandling.errorresponses.book;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class UnableToExtendNotAcceptedBookResponse extends ErrorResponse {
    public UnableToExtendNotAcceptedBookResponse() {
        super(409, "Extending not accepted book");
    }
}
