package com.system.libsystem.rest.administration.orders.orderaccept;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/administration/books/{book_id}/accept-order")
public class AcceptOrderController {

    private final AcceptOrderService acceptOrderService;

    @PutMapping
    public void confirmOrder(@RequestBody AcceptOrderRequest acceptOrderRequest,
                             @PathVariable("book_id") Long bookId) {
        acceptOrderRequest.setBorrowedBookId(bookId);
        acceptOrderService.confirmOrder(acceptOrderRequest);
    }

}

