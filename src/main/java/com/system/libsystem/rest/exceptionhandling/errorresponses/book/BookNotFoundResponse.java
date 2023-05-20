package com.system.libsystem.rest.exceptionhandling.errorresponses.book;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class BookNotFoundResponse extends ErrorResponse {
    public BookNotFoundResponse() {
        super(404, "Book not found");
    }
}
