package com.system.libsystem.rest.administration.orders.orderaccept;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AcceptOrderRequest {
    private int borrowedBookId;
    private Long cardNumber;
}
