package com.system.libsystem.rest.exceptionhandling.errorresponses.favourite;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class BookNotFoundInUserFavouriteBooksResponse extends ErrorResponse {
    public BookNotFoundInUserFavouriteBooksResponse() {
        super(404, "Book not found in favourites");
    }
}
