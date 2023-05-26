package com.system.libsystem.exceptions.book;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class UnableToExtendNotAcceptedBookException extends RuntimeException {

    private final Long id;

    @Override
    public String getMessage() {
        return "Unable to extended return date of the requested borrowed book with id " + id + " because the book is " +
                "not accepted";
    }

}
