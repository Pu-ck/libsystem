package com.system.libsystem.exceptions.cardnumber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid card number format")
public class InvalidCardNumberFormatException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Invalid card number format";
    }

}
