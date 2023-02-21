package com.system.libsystem.rest.passwordreminder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResetPasswordRequest {
    private String password;
    private String token;
}
