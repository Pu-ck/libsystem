package com.system.libsystem.rest.exceptionhandling.errorresponses.book;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class BookAlreadyReturnedResponse extends ErrorResponse {
    public BookAlreadyReturnedResponse() {
        super(409, "Book already returned");
    }
}
