package com.system.libsystem.rest.user;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
