package com.system.libsystem.rest.administration.orderconfirm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConfirmOrderRequest {
    private int borrowedBookId;
    private Long cardNumber;
}
