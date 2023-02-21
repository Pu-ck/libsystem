package com.system.libsystem.rest.passwordreminder;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home/password-reminder")
@AllArgsConstructor
public class PasswordReminderController {

    private final PasswordReminderService passwordReminderService;

    @PostMapping
    public void remindPassword(@RequestBody PasswordReminderRequest passwordReminderRequest) {
        passwordReminderService.remindPassword(passwordReminderRequest);
    }

    @PostMapping("/reset-password")
    public void changeUserPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        passwordReminderService.resetPassword(resetPasswordRequest);
    }

    @GetMapping("/new-password")
    public String getChangePasswordPage(@RequestParam("token") String token) {
        String result = passwordReminderService.validatePasswordReminderToken(token);
        if (result != null) {
            // TODO: redirect to frontend login page
            return "login";
        } else {
            // TODO: redirect to frontend password reset page
            return "reset-password";
        }
    }

}

