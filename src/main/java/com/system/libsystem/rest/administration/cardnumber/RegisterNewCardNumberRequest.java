package com.system.libsystem.rest.administration.cardnumber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterNewCardNumberRequest {
    private Long cardNumber;
    private String peselNumber;
}
