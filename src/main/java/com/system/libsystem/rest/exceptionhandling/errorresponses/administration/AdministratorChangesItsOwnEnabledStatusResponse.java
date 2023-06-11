package com.system.libsystem.rest.exceptionhandling.errorresponses.administration;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class AdministratorChangesItsOwnEnabledStatusResponse extends ErrorResponse {
    public AdministratorChangesItsOwnEnabledStatusResponse() {
        super(409, "Book not found");
    }
}
