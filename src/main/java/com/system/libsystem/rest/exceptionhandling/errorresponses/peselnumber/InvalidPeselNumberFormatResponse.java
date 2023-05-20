package com.system.libsystem.rest.exceptionhandling.errorresponses.peselnumber;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class InvalidPeselNumberFormatResponse extends ErrorResponse {
    public InvalidPeselNumberFormatResponse() {
        super(400, "Invalid PESEL number format");
    }
}
