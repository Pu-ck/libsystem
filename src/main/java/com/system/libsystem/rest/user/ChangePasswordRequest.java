package com.system.libsystem.rest.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
