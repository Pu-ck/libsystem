package com.system.libsystem.rest.administration.orders.orderready;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReadyOrderRequest {
    private int borrowedBookId;
    private boolean accepted;
}
