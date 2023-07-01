package com.system.libsystem.exceptions.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Borrowed book not found")
public class BorrowedBookNotFoundException extends RuntimeException {

    private final Long borrowedBookId;
    private final Long userId;
    private final Long cardNumber;

    @Override
    public String getMessage() {
        if (borrowedBookId != null) {
            return "Unable to find borrowed book with id: " + borrowedBookId;
        } else if (cardNumber != null) {
            return "Unable to find borrowed book associated with card number: " + cardNumber;
        } else if (userId != null) {
            return "Unable to find borrowed book associated with user with id: " + userId;
        } else {
            return "Borrowed book not found";
        }
    }

}
