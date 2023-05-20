package com.system.libsystem.exceptions.peselnumber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "PESEL number not authenticated")
public class UnableToAuthenticatePeselNumberException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Unable to authenticate PESEL number";
    }

}
