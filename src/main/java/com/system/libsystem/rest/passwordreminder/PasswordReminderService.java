package com.system.libsystem.rest.passwordreminder;

import org.springframework.http.ResponseEntity;

public interface PasswordReminderService {

    void remindPassword(PasswordReminderRequest passwordReminderRequest);

    ResponseEntity<Void> validatePasswordReminderToken(String token);

    void resetPassword(ResetPasswordRequest resetPasswordRequest);

}
