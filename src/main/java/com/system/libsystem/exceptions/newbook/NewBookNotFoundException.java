package com.system.libsystem.exceptions.newbook;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewBookNotFoundException extends RuntimeException {
    private final Long bookId;

    @Override
    public String getMessage() {
        return "Unable to find new book associated with book id " + bookId;
    }
}
