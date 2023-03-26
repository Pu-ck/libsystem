package com.system.libsystem.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookNotFoundException extends RuntimeException {

    private final int bookId;

    @Override
    public String getMessage() {
        return "Unable to find book with id " + bookId;
    }

}
