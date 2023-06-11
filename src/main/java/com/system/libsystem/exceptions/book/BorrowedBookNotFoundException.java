package com.system.libsystem.exceptions.book;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BorrowedBookNotFoundException extends RuntimeException {

    private final Long bookId;

    @Override
    public String getMessage() {
        return "Unable to find borrowed book with id " + bookId;
    }

}
