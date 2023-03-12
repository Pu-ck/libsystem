package com.system.libsystem.rest.administration.books.bookreturn;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/administration/books/{book_id}/return-book")
public class ReturnBookController {

    private final ReturnBookService returnBookService;

    @PutMapping
    public void returnBook(@RequestBody ReturnBookRequest returnBookRequest,
                           @PathVariable("book_id") int bookId) {
        returnBookRequest.setBorrowedBookId(bookId);
        returnBookService.returnBook(returnBookRequest);
    }

}
