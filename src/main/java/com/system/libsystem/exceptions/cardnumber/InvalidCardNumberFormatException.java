package com.system.libsystem.exceptions.cardnumber;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidCardNumberFormatException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Invalid card number format";
    }

}
