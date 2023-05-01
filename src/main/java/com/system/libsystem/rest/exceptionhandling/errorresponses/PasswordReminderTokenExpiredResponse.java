package com.system.libsystem.rest.exceptionhandling.errorresponses;

public class PasswordReminderTokenExpiredResponse extends ErrorResponse {
    public PasswordReminderTokenExpiredResponse() {
        super(404, "Password reminder token expired");
    }
}
