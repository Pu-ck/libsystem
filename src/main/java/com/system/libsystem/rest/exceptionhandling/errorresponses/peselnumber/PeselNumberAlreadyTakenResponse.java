package com.system.libsystem.rest.exceptionhandling.errorresponses.peselnumber;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class PeselNumberAlreadyTakenResponse extends ErrorResponse  {
    public PeselNumberAlreadyTakenResponse() {
        super(409, "PESEL number already taken");
    }
}
