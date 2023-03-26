package com.system.libsystem.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookAlreadyReturnedException extends RuntimeException {

    private final int borrowedBookId;

    @Override
    public String getMessage() {
        return "Borrowed book with id " + borrowedBookId + " already returned";
    }

}
