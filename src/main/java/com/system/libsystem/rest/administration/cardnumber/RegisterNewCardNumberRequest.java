package com.system.libsystem.rest.administration.cardnumber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterNewCardNumberRequest {
    private Long cardNumber;
    private String peselNumber;
}
