package com.system.libsystem.exceptions.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Book already accepted")
public class BookAlreadyAcceptedException extends RuntimeException {

    private final Long bookId;

    @Override
    public String getMessage() {
        return "Borrowed book with id " + bookId + " already accepted";
    }

}
