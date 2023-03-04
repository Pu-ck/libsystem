package com.system.libsystem.entities.passwordremindertoken;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PasswordReminderTokenService {

    private final PasswordReminderTokenRepository passwordReminderTokenRepository;

    public PasswordReminderTokenEntity getPasswordReminderToken(String token) throws IllegalStateException {
        return passwordReminderTokenRepository.findByToken(token).orElseThrow(
                () -> new IllegalStateException("Unable to find requested password reminder token " + token));
    }

}
