package com.system.libsystem.exceptions.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Extending not accepted book")
public class UnableToExtendNotAcceptedBookException extends RuntimeException {

    private final Long bookId;

    @Override
    public String getMessage() {
        return "Unable to extended return date of the requested borrowed book with id " + bookId + " because the book is " +
                "not accepted";
    }

}
