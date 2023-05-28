package com.system.libsystem.rest.passwordreminder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordReminderRequest {
    private String username;
    private Long cardNumber;
}
