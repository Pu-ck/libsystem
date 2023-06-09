package com.system.libsystem.rest.login;

import com.system.libsystem.util.UserType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginSessionResponse {
    private String sessionID;
    private UserType userType;
}
