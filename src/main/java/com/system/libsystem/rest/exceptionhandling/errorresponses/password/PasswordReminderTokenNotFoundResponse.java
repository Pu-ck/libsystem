package com.system.libsystem.rest.exceptionhandling.errorresponses.password;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class PasswordReminderTokenNotFoundResponse extends ErrorResponse {
    public PasswordReminderTokenNotFoundResponse() { super(404, "Password reminder token not found"); }
}
