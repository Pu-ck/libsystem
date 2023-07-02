package com.system.libsystem.rest.exceptionhandling.errorresponses.book;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class BookAlreadyExtendedResponse extends ErrorResponse {
    public BookAlreadyExtendedResponse() {
        super(409, "Book already extended");
    }
}
