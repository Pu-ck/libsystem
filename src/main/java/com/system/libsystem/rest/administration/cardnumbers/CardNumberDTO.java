package com.system.libsystem.rest.administration.cardnumbers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardNumberDTO {
    private Long id;
    private Long cardNumber;
    private Long userId;
}
