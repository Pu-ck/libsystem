package com.system.libsystem.rest.registration;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class RegistrationRequest {
    private final String username;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final Long cardNumber;
}
