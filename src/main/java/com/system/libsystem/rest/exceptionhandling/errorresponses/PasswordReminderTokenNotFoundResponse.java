package com.system.libsystem.rest.exceptionhandling.errorresponses;

public class PasswordReminderTokenNotFoundResponse extends ErrorResponse {
    public PasswordReminderTokenNotFoundResponse() { super(404, "Password reminder token not found"); }
}
