package com.system.libsystem.rest.administration.cardnumbers.registercardnumber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCardNumberRequest {
    private Long cardNumber;
    private String peselNumber;
}
