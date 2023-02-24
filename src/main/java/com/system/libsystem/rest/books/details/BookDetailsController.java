package com.system.libsystem.rest.books.details;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.rest.books.details.borrow.BorrowBookRequest;
import com.system.libsystem.rest.books.details.borrow.BorrowBookService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("/api/books/book")
public class BookDetailsController {

    private final BookDetailsService bookDetailsService;
    private final BorrowBookService borrowBookService;

    @GetMapping
    public BookEntity getBookDetails(@RequestParam("id") int bookId) {
        return bookDetailsService.getBookDetails(bookId);
    }

    @PostMapping
    public void borrow(@RequestBody BorrowBookRequest borrowBookRequest, HttpServletRequest httpServletRequest) {
        borrowBookService.borrow(borrowBookRequest, httpServletRequest);
    }
}
