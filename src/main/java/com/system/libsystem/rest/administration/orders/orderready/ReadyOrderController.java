package com.system.libsystem.rest.administration.orders.orderready;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/administration/books/{book_id}/ready-order")
public class ReadyOrderController {

    private final ReadyOrderService readyOrderService;

    @PutMapping
    public void setOrderStatus(@RequestBody ReadyOrderRequest readyOrderRequest,
                               @PathVariable("book_id") int bookId) {
        readyOrderRequest.setBorrowedBookId(bookId);
        readyOrderService.setOrderStatus(readyOrderRequest);
    }

}
