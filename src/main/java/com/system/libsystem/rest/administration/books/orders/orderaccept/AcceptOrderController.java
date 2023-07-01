package com.system.libsystem.rest.administration.books.orders.orderaccept;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("/api/administration/books/{book_id}/accept-order")
public class AcceptOrderController {

    private final AcceptOrderService acceptOrderService;

    @PutMapping
    public void confirmOrder(@RequestBody AcceptOrderRequest acceptOrderRequest,
                             @PathVariable("book_id") Long bookId,
                             HttpServletRequest httpServletRequest) {
        acceptOrderRequest.setBorrowedBookId(bookId);
        acceptOrderService.confirmOrder(acceptOrderRequest, httpServletRequest);
    }

}

