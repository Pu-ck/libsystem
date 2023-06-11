package com.system.libsystem.exceptions.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not found")
public class UserNotFoundException extends RuntimeException {

    private final Long userId;
    private final Long cardNumber;
    private final String username;

    @Override
    public String getMessage() {
        if (userId != null) {
            return "Unable to find user with ID: " + userId;
        } else if (cardNumber != null) {
            return "Unable to find user with card number: " + cardNumber;
        } else if (username != null) {
            return "Unable to find user with username: " + username;
        } else {
            return "User not found";
        }
    }

}
