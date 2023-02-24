package com.system.libsystem.rest.administration.orderconfirm;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/administration/books/confirm-order")
public class ConfirmOrderController {

    private final ConfirmOrderService confirmOrderService;

    @PostMapping
    public void confirmOrder(@RequestBody ConfirmOrderRequest confirmOrderRequest) {
        confirmOrderService.confirmOrder(confirmOrderRequest);
    }

}

