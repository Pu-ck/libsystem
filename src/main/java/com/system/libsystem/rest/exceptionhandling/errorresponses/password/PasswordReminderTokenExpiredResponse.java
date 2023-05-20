package com.system.libsystem.rest.exceptionhandling.errorresponses.password;

import com.system.libsystem.rest.exceptionhandling.errorresponses.ErrorResponse;

public class PasswordReminderTokenExpiredResponse extends ErrorResponse {
    public PasswordReminderTokenExpiredResponse() {
        super(404, "Password reminder token expired");
    }
}
