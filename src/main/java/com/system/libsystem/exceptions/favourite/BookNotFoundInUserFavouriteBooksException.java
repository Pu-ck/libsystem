package com.system.libsystem.exceptions.favourite;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Book not found in favourites")
public class BookNotFoundInUserFavouriteBooksException extends RuntimeException {

    private final Long userId;

    @Override
    public String getMessage() {
        return "Book not found in favourite books of user with id " + userId;
    }

}
