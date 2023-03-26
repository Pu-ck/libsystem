package com.system.libsystem.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BorrowedBookNotFoundException extends RuntimeException {

    private final int borrowedBookId;

    @Override
    public String getMessage() {
        return "Unable to find borrowed book with id " + borrowedBookId;
    }

}
