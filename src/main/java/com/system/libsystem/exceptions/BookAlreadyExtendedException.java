package com.system.libsystem.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookAlreadyExtendedException extends RuntimeException {

    private final int id;

    @Override
    public String getMessage() {
        return "Borrowed book with id " + id + " already extended";
    }

}