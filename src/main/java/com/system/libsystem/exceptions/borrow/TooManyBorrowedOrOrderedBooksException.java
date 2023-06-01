package com.system.libsystem.exceptions.borrow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Too many books borrowed or ordered")
public class TooManyBorrowedOrOrderedBooksException extends RuntimeException {

    private final Long id;

    @Override
    public String getMessage() {
        return "The order is exceeding borrowed or ordered books of user with id " + id;
    }

}
