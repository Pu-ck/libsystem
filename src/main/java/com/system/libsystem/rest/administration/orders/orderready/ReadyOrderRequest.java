package com.system.libsystem.rest.administration.orders.orderready;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadyOrderRequest {
    private Long borrowedBookId;
    private boolean accepted;
}
