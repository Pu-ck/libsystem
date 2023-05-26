package com.system.libsystem.rest.exceptionhandling.errorresponses.borrow;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class TooManyBorrowedBooksResponse extends ErrorResponse {
    public TooManyBorrowedBooksResponse() {
        super(400, "Too many books borrowed");
    }
}
