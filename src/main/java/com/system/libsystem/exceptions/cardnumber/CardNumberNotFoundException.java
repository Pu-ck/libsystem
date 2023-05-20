package com.system.libsystem.exceptions.cardnumber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Card number not found")
public class CardNumberNotFoundException extends RuntimeException {

    private final long cardNumber;

    @Override
    public String getMessage() {
        return "Unable to find card number " + cardNumber;
    }

}
