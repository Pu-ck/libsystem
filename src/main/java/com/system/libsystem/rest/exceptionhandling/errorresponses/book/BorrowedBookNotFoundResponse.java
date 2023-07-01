package com.system.libsystem.rest.exceptionhandling.errorresponses.book;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class BorrowedBookNotFoundResponse extends ErrorResponse {
    public BorrowedBookNotFoundResponse() {
        super(404, "Borrowed book not found");
    }
}
