package com.system.libsystem.rest.administration.orders.orderaccept;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AcceptOrderRequest {
    private Long borrowedBookId;
    private Long cardNumber;
}
