package com.system.libsystem.rest.exceptionhandling.errorresponses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public abstract class ErrorResponse {
    private int status;
    private String message;
}
