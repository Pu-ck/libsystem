package com.system.libsystem.rest.administration.books.bookreturn;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("/api/administration/books/{book_id}/return-book")
public class ReturnBookController {

    private final ReturnBookService returnBookService;

    @PutMapping
    public void returnBook(@RequestBody ReturnBookRequest returnBookRequest,
                           @PathVariable("book_id") Long bookId,
                           HttpServletRequest httpServletRequest) {
        returnBookRequest.setBorrowedBookId(bookId);
        returnBookService.returnBook(returnBookRequest, httpServletRequest);
    }

}
