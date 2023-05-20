package com.system.libsystem.exceptions.peselnumber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid PESEL number format")
public class InvalidPeselNumberFormatException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Invalid PESEL number format";
    }

}
