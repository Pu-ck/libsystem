package com.system.libsystem.exceptions.borrow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Book out of stock")
public class BookOutOfStockException extends RuntimeException {

    private final Long bookId;

    @Override
    public String getMessage() {
        return "Book with id " + bookId + " is not available in stock " + "of the library affiliate or the requested " +
                "quantity of books to borrow is larger than the quantity in stock of the library affiliate";
    }

}
