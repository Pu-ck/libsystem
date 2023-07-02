package com.system.libsystem.rest.exceptionhandling.errorresponses.book;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class BookAlreadyAcceptedResponse extends ErrorResponse {
    public BookAlreadyAcceptedResponse() {
        super(409, "Book already accepted");
    }
}
