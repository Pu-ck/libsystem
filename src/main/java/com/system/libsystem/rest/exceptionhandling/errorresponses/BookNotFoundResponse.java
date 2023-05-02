package com.system.libsystem.rest.exceptionhandling.errorresponses;

public class BookNotFoundResponse extends ErrorResponse {
    public BookNotFoundResponse() {
        super(404, "Book not found");
    }
}
