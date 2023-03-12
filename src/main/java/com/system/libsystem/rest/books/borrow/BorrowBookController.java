package com.system.libsystem.rest.books.borrow;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("/api/books")
public class BorrowBookController {

    private final BorrowBookService borrowBookService;

    @PutMapping("/{book_id}/borrow-book")
    public void borrow(@RequestBody BorrowBookRequest borrowBookRequest,
                       @PathVariable("book_id") int bookId,
                       HttpServletRequest httpServletRequest) {
        borrowBookRequest.setBookId(bookId);
        borrowBookService.borrow(borrowBookRequest, httpServletRequest);
    }

}
