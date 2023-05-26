package com.system.libsystem.rest.exceptionhandling.errorresponses.borrow;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class BookOutOfStockResponse extends ErrorResponse {
    public BookOutOfStockResponse() {
        super(404, "Book out of stock");
    }
}
