package com.system.libsystem.exceptions.registration;

public class InvalidEmailAddressFormat extends RuntimeException {

    @Override
    public String getMessage() { return "Invalid email address format"; }

}
