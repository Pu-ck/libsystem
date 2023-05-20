package com.system.libsystem.exceptions.peselnumber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "PESEL number already taken")
public class PeselNumberAlreadyTakenException extends RuntimeException {

    @Override
    public String getMessage() {
        return "PESEL number already taken";
    }

}
