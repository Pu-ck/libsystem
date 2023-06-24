package com.system.libsystem.exceptions.administration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Updating own enabled status")
public class AdministratorChangesItsOwnEnabledStatusException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Administrator cannot modify its own enabled status";
    }

}
