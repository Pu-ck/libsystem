package com.system.libsystem.exceptions.cardnumber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Username already taken")
public class CardNumberAlreadyTakenException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Card number already taken";
    }

}
