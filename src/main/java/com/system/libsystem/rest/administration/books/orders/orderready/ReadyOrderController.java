package com.system.libsystem.rest.administration.books.orders.orderready;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("/api/administration/books/{book_id}/ready-order")
public class ReadyOrderController {

    private final ReadyOrderService readyOrderService;

    @PutMapping
    public void setOrderStatus(@RequestBody ReadyOrderRequest readyOrderRequest,
                               @PathVariable("book_id") Long bookId,
                               HttpServletRequest httpServletRequest) {
        readyOrderRequest.setBorrowedBookId(bookId);
        readyOrderService.setOrderStatus(readyOrderRequest, httpServletRequest);
    }

}
