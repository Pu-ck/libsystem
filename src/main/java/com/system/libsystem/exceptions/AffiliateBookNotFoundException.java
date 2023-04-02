package com.system.libsystem.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AffiliateBookNotFoundException extends RuntimeException {

    private final String name;
    private final int bookId;

    @Override
    public String getMessage() {
        return "Unable to find affiliate " + name + " relation with book with id " + bookId;
    }

}
