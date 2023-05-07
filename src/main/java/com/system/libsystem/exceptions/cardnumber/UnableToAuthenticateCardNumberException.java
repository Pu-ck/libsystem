package com.system.libsystem.exceptions.cardnumber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Card number not authenticated")
public class UnableToAuthenticateCardNumberException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Unable to authenticate and associate the provided card number with given user";
    }

}
