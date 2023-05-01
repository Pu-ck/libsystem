package com.system.libsystem.rest.passwordreminder;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password-reminder")
@AllArgsConstructor
public class PasswordReminderController {

    private final PasswordReminderService passwordReminderService;

    @PostMapping
    public void remindPassword(@RequestBody PasswordReminderRequest passwordReminderRequest) {
        passwordReminderService.remindPassword(passwordReminderRequest);
    }

    @GetMapping("/new-password")
    public ResponseEntity<Void> validatePasswordReminderToken(@RequestParam("token") String token) {
        return passwordReminderService.validatePasswordReminderToken(token);
    }

    @PutMapping("/{token}/reset-password")
    public void changeUserPassword(@RequestBody ResetPasswordRequest resetPasswordRequest,
                                   @PathVariable("token") String token) {
        resetPasswordRequest.setToken(token);
        passwordReminderService.resetPassword(resetPasswordRequest);
    }

}

