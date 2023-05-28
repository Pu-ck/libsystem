package com.system.libsystem.rest.administration.orders.orderaccept;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AcceptOrderRequest {
    private Long borrowedBookId;
    private Long cardNumber;
}
