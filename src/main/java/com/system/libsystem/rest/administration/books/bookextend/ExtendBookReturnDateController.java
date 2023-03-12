package com.system.libsystem.rest.administration.books.bookextend;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/administration/books/{book_id}/extend-book")
public class ExtendBookReturnDateController {

    private final ExtendBookReturnDateService extendBookReturnDateService;

    @PutMapping
    public void extendRequestedBookReturnDate(@RequestBody ExtendBookReturnDateRequest extendBookReturnDateRequest,
                                              @PathVariable("book_id") int bookId) {
        extendBookReturnDateRequest.setBorrowedBookId(bookId);
        extendBookReturnDateService.extendRequestedBookReturnDate(extendBookReturnDateRequest);
    }

}
