package com.system.libsystem.rest.administration.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserEnabledStatusRequest {
    public UpdateUserEnabledStatusRequest() {
        this.reason = "";
    }

    private Long userId;
    private String reason;
}
